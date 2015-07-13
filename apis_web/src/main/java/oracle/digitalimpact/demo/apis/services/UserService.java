package oracle.digitalimpact.demo.apis.services;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import oracle.digitalimpact.demo.apis.constants.Constants;
import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.model.Context;
import oracle.digitalimpact.demo.apis.model.User;
import oracle.digitalimpact.demo.apis.threads.EmailerThread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

public class UserService implements Constants {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;

    
    private transient Log logger = LogFactory.getLog(this.getClass());
    
    private long timeInMilliSecs = 5000L;
    private MongoService mongoService = null;
    private EmailService emailService = null;
    private ThreadPoolTaskScheduler scheduler = null;
    private EmailerThread emailerThread = null;
    
    public List <User> list() throws APIException {
        logger.info("Entered");
        List <User> users = new ArrayList <User> ();
        // Retrieve collection
        DBCollection demo_users_table = getMongoService().getDb().getCollection(COLL_DEMO_USERS);
        
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("status",   STATUS_VALID);
        // Retrieve cursor for iterating records
        DBCursor cur = demo_users_table.find(searchQuery);
        
        // Iterate cursor
        while(cur.hasNext()) {
            DBObject dbObject = cur.next();
            User user = populateUser(dbObject, false);
            users.add(user);
        }
        logger.info("Leaving");
        return users;
    }

    private User populateUser(DBObject dbObject, boolean extractPin) {
        logger.info("Entered");
        // Map DBOject to User
        User user = new User ();
        user.setFirstName(dbObject.get("first_name").toString());
        user.setLastName(dbObject.get("last_name").toString());
        user.setEmail(dbObject.get("email").toString());
        if (extractPin) {
            if (dbObject.get("password_pin") != null) {
                user.setPin(dbObject.get("password_pin").toString());
            }
        }
        if (dbObject.get("picture") != null) {
            user.setPicture(dbObject.get("picture").toString());
        }
        Context context = new Context ();
        context.setName(dbObject.get("context").toString());
        BasicDBList interests = (BasicDBList) dbObject.get("interests");
        for (Object object : interests) {
            BasicDBObject interest = (BasicDBObject) object;
            context.addInterest(interest.get(ATTRIBUTE_NAME).toString());
        }
        user.setContext(context);
        user.setUsername(dbObject.get("username").toString());
        user.setStatus(dbObject.get("status").toString());
        logger.info("Leaving");
        return user;
    }
    
    public User get(String username) throws APIException {
        logger.info("Entered");
        User user = new User ();
        DBCollection table = getMongoService().getDb().getCollection(COLL_DEMO_USERS);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("username", username);
        searchQuery.put("status",   STATUS_VALID);
        DBCursor cursor = table.find(searchQuery);
        if (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            user = populateUser(dbObject, false);
        }
        else {
            throw new APIException ("User [" + username + "] not found.");
        }
        logger.info("Leaving");
        return user;
    }
    
    public User getUserForAuthentication (String username) throws APIException {
        logger.info("Entered");
        User user = new User ();
        DBCollection table = getMongoService().getDb().getCollection(COLL_DEMO_USERS);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("username", username);
        searchQuery.put("status",   STATUS_VALID);
        DBCursor cursor = table.find(searchQuery);
        if (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            user = populateUser(dbObject, true);
        }
        else {
            throw new APIException ("User [" + username + "] not found.");
        }
        logger.info("Leaving");
        return user;
    }

    public void delete(String username) throws APIException {
        logger.info("Entered");
        DBCollection table = getMongoService().getDb().getCollection(COLL_DEMO_USERS);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("username", username);
        table.remove(searchQuery);
        logger.info("Leaving");
    }
    
    public User register(User user) throws APIException {
        logger.info("Entered");
        DBCollection table          =   getMongoService().getDb().getCollection(COLL_DEMO_USERS);
        BasicDBObject searchQuery   =   new BasicDBObject();
        String username             =   user.getEmail().substring(0, user.getEmail().indexOf("@"));
        searchQuery.put("username",    username);
        DBCursor cursor = table.find(searchQuery);
        if (cursor.count() == 0) {
            BasicDBObject document = new BasicDBObject();
            document.put("first_name",  user.getFirstName());
            document.put("last_name",   user.getLastName());
            document.put("email",       user.getEmail());
            document.put("context",     user.getContext().getName());
            BasicDBList interests       = new BasicDBList();
            for(String interest : user.getContext().getInterests()){
                BasicDBObject interestDoc = new BasicDBObject();
                interestDoc.put("name", interest);
                interests.add(interestDoc);
            }
            document.put("interests",    interests);
            document.put("username",     username);
            document.put("password_pin", user.getPin());
            document.put("picture",      user.getPicture());
            document.put("status",       STATUS_INIT);
            document.put("createdDate",  new Date());
            table.insert(document);
            
            // Set the username
            user.setUsername(username);
        }
        else {
            throw new APIException ("User [" + username + "] already exist.");
        }
        logger.info("Leaving");
        return user;
    }
    
    public void postRegister() throws APIException {
        logger.info("Entered");
        DBCollection table = getMongoService().getDb().getCollection(COLL_DEMO_USERS);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("status", STATUS_INIT);
        DBCursor cursor = table.find(searchQuery);
        if (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            User user = populateUser(dbObject, false);
            user.setPin (dbObject.get("password_pin").toString());
            // Send Email
            getEmailService().email(user.getEmail(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getPin());
            
            // Update User
            updatePostRegister (user);
            
        }
        logger.info("Leaving");
    }
    
    public User update (User user) throws APIException {
        logger.info("Entered");
        DBCollection table = getMongoService().getDb().getCollection(COLL_DEMO_USERS);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("username", user.getUsername());
        searchQuery.put("status", STATUS_VALID);
        DBCursor cursor = table.find(searchQuery);
        if (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
           
            // Update Record
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("username",     user.getUsername());
            newDocument.put("password_pin", user.getPin());
            newDocument.put("context",      user.getContext().getName());
            BasicDBList interests       = new BasicDBList();
            for(String interest : user.getContext().getInterests()){
                BasicDBObject interestDoc = new BasicDBObject();
                interestDoc.put("name", interest);
                interests.add(interestDoc);
            }
            newDocument.put("interests", interests);
            newDocument.put("picture", user.getPicture());
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.put("$set", newDocument);
            table.update(dbObject, updateObj);
        }
        logger.info("Leaving");
        return user;
    }
    
    public User updatePostRegister(User user) throws APIException {
        logger.info("Entered");
        DBCollection table = getMongoService().getDb().getCollection(COLL_DEMO_USERS);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("username", user.getUsername());
        searchQuery.put("status", STATUS_INIT);
        DBCursor cursor = table.find(searchQuery);
        if (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            user = populateUser(dbObject, false);
            
            // Update Record
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("username", user.getUsername());
            newDocument.put("status", STATUS_VALID);
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.put("$set", newDocument);
            table.update(dbObject, updateObj);
        }
        else {
            throw new APIException ("User [" + user.getUsername() + "] not found.");
        }
        logger.info("Leaving");
        return user;
    }

    public void init () throws APIException {
        logger.info("Entered");
        try {
            if (emailerThread == null) {
                emailerThread = new EmailerThread (this);
            }
            scheduler.scheduleAtFixedRate(emailerThread, getTimeInMilliSecs());
        }
        catch (Exception expGeneral) {
            throw new APIException ("Exception instantiating " + this, expGeneral);
        }
        logger.info("Leaving");
    }
    
    public void destroy () {
        logger.info("Entered");
        try {
            scheduler.getScheduledExecutor().shutdown();
            scheduler.getScheduledExecutor().awaitTermination(1000, TimeUnit.MILLISECONDS);
            scheduler.shutdown();
            scheduler.destroy();
            if (emailerThread != null) {
                emailerThread = null;
            }
        }
        catch (Exception expGeneral) {
            new APIException ("Exception destroying " + this, expGeneral);
        }
        logger.info("Leaving");
    }
    
    public void setMongoService(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public MongoService getMongoService() {
        return mongoService;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public EmailService getEmailService() {
        return emailService;
    }

    public void setScheduler(ThreadPoolTaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public ThreadPoolTaskScheduler getScheduler() {
        return scheduler;
    }


    public void setTimeInMilliSecs(long timeInMilliSecs) {
        this.timeInMilliSecs = timeInMilliSecs;
    }

    public long getTimeInMilliSecs() {
        return timeInMilliSecs;
    }

}

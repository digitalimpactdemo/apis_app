package oracle.digitalimpact.demo.apis.services;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import oracle.digitalimpact.demo.apis.constants.Constants;
import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.model.Beacon;
import oracle.digitalimpact.demo.apis.model.Engage;
import oracle.digitalimpact.demo.apis.model.NotificationItem;
import oracle.digitalimpact.demo.apis.model.NotificationRequest;
import oracle.digitalimpact.demo.apis.model.NotificationResponse;
import oracle.digitalimpact.demo.apis.model.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NotificationService implements Constants {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());
    
    private static final Random rand = new Random();
    
    private MongoService mongoService = null;
    private UserService userService = null;

    public void setMongoService(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public MongoService getMongoService() {
        return mongoService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserService getUserService() {
        return userService;
    }

    public NotificationResponse getNotifications(NotificationRequest notificationRequest) throws APIException {
        logger.info("Entered");
        NotificationResponse notificationResponse = new NotificationResponse ();
        User user = null;
        try {
            logger.warn("username :" + notificationRequest.getUsername());
            user = getUserService().get(notificationRequest.getUsername());
        } catch (APIException expAPI) {
            logger.warn("user not found.");
        }
        logger.info("user:" + user);
        if (user == null || user.getContext().getInterests().isEmpty()) {
            return notificationResponse;
        }
        logger.info ("user interests:" + user.getContext().getInterests());
        List <String> interests = new ArrayList <String> ();
        logger.info ("beacons:" + notificationRequest.getBeacons());
        for (Beacon beacon : notificationRequest.getBeacons()) {
            if (beacon.getProximity().equals(BEACON_PROXIMITY_NEAR) || beacon.getProximity().equals(BEACON_PROXIMITY_IMMEDIATE)) {
                String major_minor = beacon.getMajor() + "#" + beacon.getMinor();
                populateMatchingInterests (major_minor, user.getContext().getInterests(), interests);
            }
        }
        logger.info ("interests:" + interests);
        
        DBCollection table = getMongoService().getDb().getCollection(COLL_MESSAGES);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("tags.name",   new BasicDBObject("$in", interests));
        DBCursor cursor = table.find(searchQuery);
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            logger.info (dbObject);
            NotificationItem item = new NotificationItem  ();
            item.setTitle(dbObject.get("message").toString());
            item.setUrl(dbObject.get("message_link").toString());
            item.setDate(dbObject.get(ATTRIBUTE_CREATED_DATE).toString());
            notificationResponse.addItem(item);
        }
        logger.info("Leaving");
        return notificationResponse;
    }

    public NotificationResponse getEngage(NotificationRequest notificationRequest) throws APIException {
        logger.info("Entered");
        NotificationResponse notificationResponse = new NotificationResponse ();
        User user = null;
        try {
            logger.warn("username :" + notificationRequest.getUsername());
            user = getUserService().get(notificationRequest.getUsername());
        } catch (APIException expAPI) {
            logger.warn("user not found.");
        }
        logger.info("user:" + user);
        if (user == null || user.getContext().getInterests().isEmpty()) {
            return notificationResponse;
        }
        logger.info ("user interests:" + user.getContext().getInterests());
        DBCollection table = getMongoService().getDb().getCollection(COLL_ENGAGE);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("context",   user.getContext().getName());
        DBCursor cursor = table.find(searchQuery);
        List <Engage> engages = new ArrayList <Engage> ();
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            logger.info (dbObject);
            Engage engage = new Engage ();
            engage.setTitle(dbObject.get("title").toString());
            
            BasicDBList choices = (BasicDBList) dbObject.get("choices");
            for (Object object : choices) {
                BasicDBObject choice = (BasicDBObject) object;
                engage.addChoice(choice.get("value").toString());
            }
            engages.add(engage);
        }
        
        
        int index = randomInt(0, (engages.size()-1));
        notificationResponse.setEngage(engages.get(index));
        logger.info("Leaving");
        return notificationResponse;
    }

    private void populateMatchingInterests(String major_minor, List<String> userInterests, List<String> interests) throws APIException {
        DBCollection table = getMongoService().getDb().getCollection(COLL_CONTEXT_BEACON_MAPPING);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(ATTRIBUTE_MAJOR_MINOR, major_minor);
        DBCursor cursor = table.find(searchQuery);
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            String subcontext = dbObject.get(ATTRIBUTE_SUCONTEXT).toString();
            if (userInterests.indexOf(subcontext) != -1 && interests.indexOf(subcontext) == -1) {
                interests.add (subcontext);
            }
        }
    }
    
    private int randomInt(int min, int max) {

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}

package oracle.digitalimpact.demo.apis.services;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import oracle.digitalimpact.demo.apis.constants.Constants;
import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.model.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ContextService implements Constants {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());

    private MongoService mongoService = null;
    
    public List <Context> list() throws APIException {
        logger.info("Entered");
        List <Context> contexts = new ArrayList <Context> ();
        // Retrieve collection
        DBCollection context_master_table = getMongoService().getDb().getCollection(COLL_CONTEXT_MASTER);
        // Retrieve cursor for iterating records
        DBCursor cur = context_master_table.find();
        // Iterate cursor
        while(cur.hasNext()) {
            // Map DBOject to Context
            DBObject dbObject = cur.next();
            Context context = new Context ();
            context.setName(dbObject.get(ATTRIBUTE_NAME).toString());
            BasicDBList interests = (BasicDBList) dbObject.get(ATTRIBUTE_SUCONTEXTS);
            for (Object object : interests) {
                BasicDBObject interest = (BasicDBObject) object;
                context.addInterest(interest.get(ATTRIBUTE_NAME).toString());
            }
            contexts.add(context);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("contexts:" + contexts);
        }
        System.out.println ("contexts:" + contexts);
        logger.info("Leaving");
        return contexts;
    }
    
    public String create(String contextname) throws APIException {
        logger.info("Entered");
        List <Context> contexts = list();
        for (Context context : contexts) {
            if (context.getName().equalsIgnoreCase(contextname)) {
                logger.warn("Context [" + contextname + "] already exists.");
                return contextname;
            }
        }
        DBCollection table = getMongoService().getDb().getCollection(COLL_CONTEXT_MASTER);
        BasicDBObject document = new BasicDBObject();
        document.put(ATTRIBUTE_NAME, contextname);
        document.put(ATTRIBUTE_CREATED_DATE,  new Date());
        table.insert(document);
        logger.info("Leaving");
        return contextname;
    }
    
    public void delete(String contextname) throws APIException {
        logger.info("Entered");
        DBCollection table = getMongoService().getDb().getCollection(COLL_CONTEXT_MASTER);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(ATTRIBUTE_NAME, contextname);
        table.remove(searchQuery);
        logger.info("Leaving");
    }

    public void setMongoService(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public MongoService getMongoService() {
        return mongoService;
    }
    
}

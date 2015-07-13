package oracle.digitalimpact.demo.apis.services;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

import java.io.File;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import oracle.digitalimpact.demo.apis.constants.Constants;
import oracle.digitalimpact.demo.apis.exceptions.APIException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MongoService implements Constants {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());
    
    private transient MongoClient mongoClient   =   null;
    private transient DB db                     =   null;
    private String serverHost                   =   null;
    private String databaseName                 =   null;
    private boolean recreateDatabase            =   false;
    private List <String> collectionNames       =   new ArrayList <String> ();
    private int serverPort                      =   0;
    
    public void init () throws APIException {
        logger.info("Entered");
        try {
            mongoClient  =   new MongoClient ( getServerHost() , getServerPort() );
            db           =   mongoClient.getDB(getDatabaseName());
            for (String collectionName : getCollectionNames()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Recreate Database:" + isRecreateDatabase());
                }
                if (isRecreateDatabase() && db.collectionExists(collectionName)) {
                    DBCollection collection = db.getCollection(collectionName);
                    collection.drop();
                }
                boolean collectionExists = db.collectionExists(collectionName);
                if (logger.isDebugEnabled()) {
                    logger.debug("Collection [" + collectionName + "] exists? : " + collectionExists);
                }
                if (!collectionExists) {
                    db.createCollection(collectionName, new BasicDBObject());
                    File file = new File (System.getProperty("user.dir") + File.separatorChar + "config/schemas/" + collectionName + ".json");
                    if (file.exists()) {
                        String jsonString = FileUtils.readFileToString(file);
                        BasicDBList list = (BasicDBList) JSON.parse(jsonString);
                        DBCollection table = db.getCollection(collectionName);
                        for (Object object : list) {
                            BasicDBObject document = (BasicDBObject) object;
                            if (collectionName.equalsIgnoreCase(COLL_CONTEXT_BEACON_MAPPING)) {
                                Iterator iter = document.keySet().iterator();
                                String major_minor = null;
                                String subcontext = null;
                                while (iter.hasNext()) {
                                    String key = (String) iter.next();
                                    String val = (String) document.get (key);
                                    if (ATTRIBUTE_BEACON_ID.equalsIgnoreCase(key)) {
                                        major_minor = getBeaconMajorMinor (val);
                                    }
                                    else if (ATTRIBUTE_SUCONTEXT_ID.equalsIgnoreCase(key)) {
                                        subcontext = getSubContext(val);
                                    }
                                }
                                document = new BasicDBObject();
                                document.put(ATTRIBUTE_SUCONTEXT,   subcontext);
                                document.put(ATTRIBUTE_MAJOR_MINOR, major_minor);
                                document.put(ATTRIBUTE_CREATED_DATE, new Date());
                            }
                            table.insert(document);
                        }
                    }
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("collections:" + db.getCollectionNames());
            }
        }
        catch (Exception expGeneral) {
            throw new APIException ("Exception instantiating " + this, expGeneral);
        }
        logger.info("Leaving");
    }
    
    public String getBeaconMajorMinor (String _id) throws APIException {
        logger.info("Entered");
        String info = null;
        DBCollection table = db.getCollection(COLL_BEACON_MASTER);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(ATTRIBUTE_BEACONS + "." + ATTRIBUTE_ID, new BasicDBObject("$eq", _id));
        DBCursor cursor = table.find(searchQuery);
        if (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            BasicDBList beacons = (BasicDBList) dbObject.get(ATTRIBUTE_BEACONS);
            for (Object object : beacons) {
                BasicDBObject beacon = (BasicDBObject) object;
                if (((String)beacon.get(ATTRIBUTE_ID)).equalsIgnoreCase(_id)) {
                    info = beacon.get(ATTRIBUTE_MAJOR).toString() + "#" + beacon.get(ATTRIBUTE_MINOR).toString();
                }
            }
        }
        logger.info("Leaving");
        return info;
    }
    
    public String getSubContext (String _id) throws APIException {
        logger.info("Entered");
        String subcontext = null;
        DBCollection table = db.getCollection(COLL_CONTEXT_MASTER);
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put(ATTRIBUTE_SUCONTEXTS + "." + ATTRIBUTE_ID, new BasicDBObject("$eq", _id));
        DBCursor cursor = table.find(searchQuery);
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            BasicDBList subcontexts = (BasicDBList) dbObject.get(ATTRIBUTE_SUCONTEXTS);
            for (Object object : subcontexts) {
                BasicDBObject subcontextObject = (BasicDBObject) object;
                if (((String)subcontextObject.get(ATTRIBUTE_ID)).equalsIgnoreCase(_id)) {
                    subcontext = subcontextObject.get(ATTRIBUTE_NAME).toString();
                }
            }
        }
        logger.info("Leaving");
        return subcontext;
    }
    
    public void destroy () {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    public DB getDb() {
        return db;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setCollectionNames(List<String> collectionNames) {
        this.collectionNames = collectionNames;
    }

    public List<String> getCollectionNames() {
        return collectionNames;
    }


    public void setRecreateDatabase(boolean recreateDatabase) {
        this.recreateDatabase = recreateDatabase;
    }

    public boolean isRecreateDatabase() {
        return recreateDatabase;
    }
}

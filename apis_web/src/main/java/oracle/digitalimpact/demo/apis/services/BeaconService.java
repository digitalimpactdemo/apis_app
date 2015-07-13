package oracle.digitalimpact.demo.apis.services;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

import oracle.digitalimpact.demo.apis.constants.Constants;
import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.model.Region;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeaconService implements Constants {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());
    
    private MongoService mongoService = null;
    
    public List <Region> listRegions () throws APIException {
        logger.info("Entered");
        List <Region> regions = new ArrayList <Region> ();
        // Retrieve collection
        DBCollection context_master_table = getMongoService().getDb().getCollection(COLL_BEACON_MASTER);
        // Retrieve cursor for iterating records
        DBCursor cur = context_master_table.find();
        // Iterate cursor
        while (cur.hasNext()) {
            // Map DBOject to Context
            DBObject dbObject = cur.next();
            Region region = new Region ();
            region.setActive(Boolean.parseBoolean(dbObject.get(ATTRIBUTE_ACTIVE).toString()));
            region.setUuid(dbObject.get(ATTRIBUTE_UUID).toString());
            region.setUuname(dbObject.get(ATTRIBUTE_UUNAME).toString());
            regions.add(region);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("regions:" + regions);
        }
        
        logger.info("Leaving");
        return regions;
    }
    
    public void setMongoService(MongoService mongoService) {
        this.mongoService = mongoService;
    }

    public MongoService getMongoService() {
        return mongoService;
    }

}

package oracle.digitalimpact.demo.apis.constants;

import java.io.Serializable;


public interface Constants extends Serializable {
    
    public static final String COLL_CONTEXT_MASTER = "context_master";
    public static final String COLL_CONTEXT_BEACON_MAPPING = "context_beacon_mapping";
    public static final String COLL_BEACON_MASTER = "beacon_master";
    public static final String COLL_DEMO_USERS = "demo_users";
    public static final String COLL_MESSAGES = "messages";
    public static final String COLL_ENGAGE = "engage";
    
    
    public static final String ATTRIBUTE_ID             =   "_id";
    public static final String ATTRIBUTE_NAME           =   "name";
    public static final String ATTRIBUTE_CREATED_DATE   =   "created_date";
    public static final String ATTRIBUTE_UUID           =   "uuid";
    public static final String ATTRIBUTE_UUNAME         =   "uuname";
    public static final String ATTRIBUTE_ACTIVE         =   "active";
    public static final String ATTRIBUTE_MAJOR          =   "major";
    public static final String ATTRIBUTE_MINOR          =   "minor";
    public static final String ATTRIBUTE_MAJOR_MINOR    =   ATTRIBUTE_MAJOR + "_" + ATTRIBUTE_MINOR;
    public static final String ATTRIBUTE_BEACONS        =   "beacons";
    public static final String ATTRIBUTE_BEACON_ID      =   "beacon_id";
    public static final String ATTRIBUTE_SUCONTEXT_ID   =   "subcontext_id";
    public static final String ATTRIBUTE_SUCONTEXT      =   "subcontext";
    public static final String ATTRIBUTE_SUCONTEXTS     =   "subcontexts";
    
    public static final String CONST_REQUEST_TYPE_DECISION  =   "decision";
    public static final String CONST_REQUEST_TYPE_DATA  =   "data";
    public static final String CONST_DATA_TYPE_NOTIFICATIONS  =   "notifications";
    public static final String CONST_DATA_TYPE_ENGAGE  =   "engage";
    
    
    public static final String STATUS_INIT     = "INIT";
    public static final String STATUS_VALID    = "VALID";
    
    public static final String BEACON_PROXIMITY_NEAR        =   "ProximityNear";
    public static final String BEACON_PROXIMITY_FAR         =   "ProximityFar";
    public static final String BEACON_PROXIMITY_IMMEDIATE   =   "ProximityImmediate";
    
}

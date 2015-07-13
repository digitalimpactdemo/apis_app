package oracle.digitalimpact.demo.apis.threads;

import java.io.Serializable;

import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.services.UserService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EmailerThread extends Thread implements Serializable {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    private transient Log logger = LogFactory.getLog(this.getClass());
    
    private UserService userService = null;

    public EmailerThread(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run() {
        logger.info("Entered");
        try {
            getUserService().postRegister();
        }
        catch (APIException expAPI) {
            logger.error("Error in Emailer Thread", expAPI);
        }
        logger.info("Leaving");
    }


    private UserService getUserService () {
        return userService;
    }
}

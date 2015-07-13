package oracle.digitalimpact.demo.apis.context;

import oracle.digitalimpact.demo.apis.services.BeaconService;
import oracle.digitalimpact.demo.apis.services.ContextService;
import oracle.digitalimpact.demo.apis.services.EmailService;
import oracle.digitalimpact.demo.apis.services.NotificationService;
import oracle.digitalimpact.demo.apis.services.RSSParserService;
import oracle.digitalimpact.demo.apis.services.SecurityService;
import oracle.digitalimpact.demo.apis.services.UserService;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class APIContextAware implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;
         
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APIContextAware.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static ContextService getContextService () {
        return (ContextService) applicationContext.getBean("apis.services.contextservice");
    }
    
    public static UserService getUserService () {
        return (UserService) applicationContext.getBean("apis.services.userservice");
    }
    
    public static BeaconService getBeaconService () {
        return (BeaconService) applicationContext.getBean("apis.services.beaconservice");
    }
    
    public static SecurityService getSecurityService () {
        return (SecurityService) applicationContext.getBean("apis.services.securityservice");
    }
    
    public static EmailService getEmailService () {
        return (EmailService) applicationContext.getBean("apis.services.emailservice");
    }
    
    public static RSSParserService getRSSService () {
        return (RSSParserService) applicationContext.getBean("apis.services.rssparserservice");
    }
    
    public static NotificationService getNotificationService () {
        return (NotificationService) applicationContext.getBean("apis.services.notificationservice");
    }
}

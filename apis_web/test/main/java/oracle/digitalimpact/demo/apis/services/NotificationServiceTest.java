package oracle.digitalimpact.demo.apis.services;

import oracle.digitalimpact.demo.apis.AbstractTestCase;
import oracle.digitalimpact.demo.apis.context.APIContextAware;
import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.model.Beacon;
import oracle.digitalimpact.demo.apis.model.NotificationRequest;
import oracle.digitalimpact.demo.apis.model.NotificationResponse;

import org.junit.Test;

public class NotificationServiceTest extends AbstractTestCase {
    /**
     * @see NotificationService#getNotifications(oracle.digitalimpact.demo.apis.model.NotificationRequest)
     */
    @Test
    public void testGetNotificationsForInvalidUser() {
        try {
            NotificationRequest notificationRequest = new NotificationRequest ();
            notificationRequest.setUsername("Invalid.User");
            NotificationResponse response = APIContextAware.getNotificationService().getNotifications(notificationRequest);
            assertNotNull("Response is null.", response);
        } catch (APIException e) {
            fail ("Do not expect APIException");
        }
    }
    
    /**
     * @see NotificationService#getNotifications(oracle.digitalimpact.demo.apis.model.NotificationRequest)
     */
    @Test
    public void testGetNotificationsForValidUser() {
        try {
            
            NotificationRequest notificationRequest = new NotificationRequest ();
            notificationRequest.setUsername("paz.periasamy");
            notificationRequest.addBeacon(new Beacon ("29676", "55658", "Immediate"));
            notificationRequest.addBeacon(new Beacon ("8294", "54931", "Immediate"));
            notificationRequest.addBeacon(new Beacon ("42937", "53996", "Near"));
            
            NotificationResponse response = APIContextAware.getNotificationService().getNotifications(notificationRequest);
            assertNotNull("Response is null.", response);
            assertFalse("Response Notifications are empty.", response.getNotifications().isEmpty());
        } catch (APIException e) {
            fail ("Do not expect APIException");
        }
    }
}

package oracle.digitalimpact.demo.apis.controllers;

import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.Produces;

import oracle.digitalimpact.demo.apis.context.APIContextAware;
import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.model.NotificationRequest;
import oracle.digitalimpact.demo.apis.model.NotificationResponse;
import oracle.digitalimpact.demo.apis.services.NotificationService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Controller
public class NotificationController extends AbstractController {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/notifications", method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HttpEntity<NotificationResponse> getNotifications(@RequestBody NotificationRequest notificationRequest, HttpServletResponse response) throws APIException {
        if (notificationRequest.getUsername() == null) {
            // TODO to fix
            notificationRequest.setUsername("clarence.cheah");
        }
        NotificationResponse notificationResponse = new NotificationResponse ();
        System.out.println(notificationRequest);
        if (notificationRequest.getType().equalsIgnoreCase(CONST_REQUEST_TYPE_DATA)) {
            if (notificationRequest.getDataType().equalsIgnoreCase(CONST_DATA_TYPE_NOTIFICATIONS)) {
                notificationResponse = getNotificationService().getNotifications (notificationRequest);
            }
            else {
                notificationResponse = getNotificationService().getEngage (notificationRequest);
            }            
        }
        else if (notificationRequest.getType().equalsIgnoreCase(CONST_REQUEST_TYPE_DECISION)) {
            if (notificationRequest.getBeacons().size() == 0) {
                notificationResponse.setTarget("landing");
            }
            else {
                notificationResponse.setTarget("notifications");
            }
        }
        return new HttpEntity<NotificationResponse>(notificationResponse, getHttpHeaders());
    }

    private NotificationService getNotificationService () {
        return APIContextAware.getNotificationService();
    }
}

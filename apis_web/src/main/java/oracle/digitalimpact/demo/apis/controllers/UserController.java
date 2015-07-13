package oracle.digitalimpact.demo.apis.controllers;

import java.util.List;

import javax.ws.rs.Produces;

import oracle.digitalimpact.demo.apis.context.APIContextAware;
import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.model.APIResponse;
import oracle.digitalimpact.demo.apis.model.User;
import oracle.digitalimpact.demo.apis.services.UserService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class UserController extends AbstractController {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HttpEntity<List <User>> getUsers() throws APIException {
        return new HttpEntity<List <User>>(getUserService().list(), getHttpHeaders());
    }
    
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/user/{username:.+}", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HttpEntity<User> getUser(@PathVariable String username) throws APIException {
        return new HttpEntity<User>(getUserService().get(username), getHttpHeaders());
    }
    
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/user/{username:.+}", method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HttpEntity<APIResponse> save(@RequestBody User user) {
        System.out.println ("User:" + user);
        try {
            getUserService().update(user);
        }
        catch (APIException expAPI) {
            return new HttpEntity<APIResponse> (new APIResponse("error", expAPI.getErrorMessage()), getHttpHeaders());    
        }
        return new HttpEntity<APIResponse> (new APIResponse("success", null), getHttpHeaders());
    }
    
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HttpEntity<APIResponse> register(@RequestBody User user) {
        System.out.println ("User:" + user);
        try {
            getUserService().register(user);
        }
        catch (APIException expAPI) {
            return new HttpEntity<APIResponse> (new APIResponse("error", expAPI.getErrorMessage()), getHttpHeaders());    
        }
        return new HttpEntity<APIResponse> (new APIResponse("success", null), getHttpHeaders());
    }

    private UserService getUserService () {
        return APIContextAware.getUserService();
    }
    
}

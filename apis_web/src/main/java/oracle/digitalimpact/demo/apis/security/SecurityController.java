package oracle.digitalimpact.demo.apis.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.Produces;

import oracle.digitalimpact.demo.apis.context.APIContextAware;
import oracle.digitalimpact.demo.apis.controllers.AbstractController;
import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.model.Security;
import oracle.digitalimpact.demo.apis.services.SecurityService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class SecurityController extends AbstractController {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HttpEntity<String> login() throws APIException {
        return new HttpEntity<String>("OK", getHttpHeaders());
    }
    
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HttpEntity<String> logout(HttpServletRequest request,
                        HttpServletResponse response) throws APIException {
        request.getSession().invalidate();
        return new HttpEntity<String>("Logged Out", getHttpHeaders());
    }
    
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/authorise", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HttpEntity<Security> authorise(HttpServletRequest request) throws APIException {
        return new HttpEntity<Security>(getSecurityService().authorise(request.getUserPrincipal().getName()), getHttpHeaders());
    }
    
    private SecurityService getSecurityService () {
        return APIContextAware.getSecurityService();
    }

}

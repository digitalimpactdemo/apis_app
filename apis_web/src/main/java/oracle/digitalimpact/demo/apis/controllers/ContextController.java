package oracle.digitalimpact.demo.apis.controllers;

import java.util.List;

import javax.ws.rs.Produces;

import oracle.digitalimpact.demo.apis.context.APIContextAware;
import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.model.Context;
import oracle.digitalimpact.demo.apis.services.ContextService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ContextController extends AbstractController {
    
    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/contexts", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HttpEntity<List <Context>> getContexts() throws APIException {
        return new HttpEntity<List <Context>>(getContextService().list(), getHttpHeaders());
    }
    
    private ContextService getContextService () {
        return APIContextAware.getContextService();
    }
    
}

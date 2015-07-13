package oracle.digitalimpact.demo.apis.controllers;

import java.util.List;

import javax.ws.rs.Produces;

import oracle.digitalimpact.demo.apis.context.APIContextAware;
import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.model.Region;
import oracle.digitalimpact.demo.apis.services.BeaconService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@Controller
public class BeaconController extends AbstractController {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/regions", method = RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody HttpEntity<List <Region>> getRegionIdentifier() throws APIException {
        return new HttpEntity<List <Region>>(getBeaconService().listRegions(), getHttpHeaders());
    }
    
    private BeaconService getBeaconService () {
        return APIContextAware.getBeaconService();
    }
}

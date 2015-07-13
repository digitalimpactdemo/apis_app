package oracle.digitalimpact.demo.apis.controllers;

import java.io.Serializable;

import oracle.digitalimpact.demo.apis.constants.Constants;

import org.springframework.http.HttpHeaders;

public class AbstractController implements Serializable, Constants {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;

    protected HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Headers", "Content-Type");
        headers.set("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        return headers;
    }
    
}

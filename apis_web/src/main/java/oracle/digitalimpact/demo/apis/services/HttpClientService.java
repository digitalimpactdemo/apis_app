package oracle.digitalimpact.demo.apis.services;

import java.io.Serializable;

import oracle.digitalimpact.demo.apis.exceptions.APIException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpClientService implements Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());
    
    public byte [] getContent (String url) throws APIException {
        logger.info("Entered");
        
        byte[] responseBody = null;
            
        // Create an instance of HttpClient.
        HttpClient client = new HttpClient();

        // Create a method instance.
        GetMethod method = new GetMethod(url);

        // Provide custom retry handler is necessary
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

        try {

            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
              System.err.println("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            responseBody = method.getResponseBody();

        }
        catch (Exception expGeneral) {
            throw new APIException ("Exception getting content from " + url, expGeneral);
        }
        logger.info("Leaving");
        return responseBody;
    }
    
    
}

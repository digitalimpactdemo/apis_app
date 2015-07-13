package oracle.digitalimpact.demo.apis.services;

import junit.framework.TestCase;

import oracle.digitalimpact.demo.apis.exceptions.APIException;

import org.junit.Test;

public class HttpClientServiceTest extends TestCase {
    


    /**
     * @see HttpClientService#getContent(String)
     */
    @Test
    public void testGetContent() {
        try {
            HttpClientService service = new HttpClientService ();
            byte [] content = service.getContent("http://www.apache.org/");
            assertFalse("Content object not null.", (content == null));
            String contentHTML = new String (content);
            assertFalse("Content object not null.", (contentHTML == null));
            assertTrue("Content did not contain required information.", contentHTML.contains("Apache Software Foundation"));
        } catch (APIException expAPI) {
            fail ("Do not expect APIException");
        }
    }
}

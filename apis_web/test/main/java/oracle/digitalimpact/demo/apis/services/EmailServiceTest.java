package oracle.digitalimpact.demo.apis.services;

import oracle.digitalimpact.demo.apis.context.APIContextAware;
import oracle.digitalimpact.demo.apis.exceptions.APIException;

import org.junit.After;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class EmailServiceTest {

    private static ApplicationContext appContext = null;

    @Before
    public void setUp() throws Exception {
        if (appContext == null) {
            appContext = new ClassPathXmlApplicationContext ("classpath:/config/spring/spring_config_mail_service.xml");
            APIContextAware contextAware = new APIContextAware ();
            contextAware.setApplicationContext(appContext);
        }
    }


    @After
    public void tearDown() throws Exception {
    }

    /**
     * @see EmailService#email(String,String,String,String,String)
     */
    @Test
    public void testEmail() {
        try {
            System.out.println (APIContextAware.getEmailService().getFromAddress());
            APIContextAware.getEmailService().email("paz.periasamy@oracle.com", "Paz", "Periasamy", "paz.periasamy", "0000");
        } catch (APIException e) {
            fail ("Do not expect APIException");
        }
    }
}

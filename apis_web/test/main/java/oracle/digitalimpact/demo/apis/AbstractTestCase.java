package oracle.digitalimpact.demo.apis;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AbstractTestCase extends TestCase {
    
    static {
        System.setProperty("user.dir", "E:\\APPS\\Oracle\\Middleware\\user_projects\\domains\\portal_domain");
    }
    
    
    private static ConfigurableApplicationContext appContext = null;

    @Before
    public void setUp() throws Exception {
        if (appContext == null) {
            appContext = new ClassPathXmlApplicationContext ("classpath:/config/spring/spring_config_api_services.xml");
        }
        
    }

    @After
    public void tearDown() throws Exception {
        //appContext.close();
    }
}

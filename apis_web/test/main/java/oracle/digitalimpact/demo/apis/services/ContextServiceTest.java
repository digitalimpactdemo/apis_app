package oracle.digitalimpact.demo.apis.services;

import java.util.List;

import oracle.digitalimpact.demo.apis.AbstractTestCase;
import oracle.digitalimpact.demo.apis.context.APIContextAware;
import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.model.Context;

import org.junit.Test;

public class ContextServiceTest extends AbstractTestCase {
    
    /**
     * @see ContextService#list()
     */
    @Test
    public void testList() {
        try {
            List <Context> contexts = APIContextAware.getContextService().list();
            assertTrue("Contexts are empty.", (contexts.size() > 0));
        } catch (APIException e) {
            fail ("Do not expect APIException");
        }
    }

    /**
     * @see ContextService#create(java.lang.String)
     */
    @Test
    public void testCreate() {
        try {
            String contextName = "TestContextName" + System.currentTimeMillis();
            List <Context> contexts = APIContextAware.getContextService().list();
            int size = contexts.size();
            assertTrue("Contexts are empty.", (size > 0));
            APIContextAware.getContextService().create(contextName);
            contexts = APIContextAware.getContextService().list();
            assertTrue("Contexts are empty.", (size > 0));
            assertEquals("Number of contexts does not match.", (size + 1), contexts.size());
            APIContextAware.getContextService().create(contextName);
            contexts = APIContextAware.getContextService().list();
            assertTrue("Contexts are empty.", (size > 0));
            assertEquals("Number of contexts does not match.", (size + 1), contexts.size());
            APIContextAware.getContextService().delete(contextName);
            contexts = APIContextAware.getContextService().list();
            assertTrue("Contexts are empty.", (size > 0));
            assertEquals("Number of contexts does not match.", size, contexts.size());

        } catch (APIException e) {
            fail ("Do not expect APIException");
        }
    }
}

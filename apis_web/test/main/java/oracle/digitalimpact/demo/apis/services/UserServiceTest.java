package oracle.digitalimpact.demo.apis.services;

import java.util.List;

import oracle.digitalimpact.demo.apis.AbstractTestCase;
import oracle.digitalimpact.demo.apis.context.APIContextAware;
import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.model.Context;
import oracle.digitalimpact.demo.apis.model.User;

import org.junit.Test;

public class UserServiceTest extends AbstractTestCase {
    
    /**
     * @see UserService#register(oracle.digitalimpact.demo.apis.model.Registration)
     */
    @Test
    public void testRegisterForValidRegistration() {
        try {
            String username = "paz.periasamy";
            APIContextAware.getUserService().delete(username);
            try {
                User user = APIContextAware.getUserService().get(username);
                fail ("User shouldnt exist.");
            }
            catch (APIException expAPI) {
                assertEquals("username still exists.", "User [" + username + "] not found.", expAPI.getErrorMessage());
            }
            User user = getValidUser();
            user = APIContextAware.getUserService().register(user);
            assertNotNull("Registration object not null.", user);
            assertNotNull("Registration username not null.", user.getUsername());
            assertEquals("Registration username does not match.", username, user.getUsername());
            
            user = getValidUser();
            try {
                user = APIContextAware.getUserService().register(user);
            }
            catch (APIException expAPI) {
                assertEquals("username does not exist.", "User [" + username + "] already exist.", expAPI.getErrorMessage());
            }
        } catch (APIException e) {
            fail ("Do not expect APIException");
        }
    }
    
    @Test
    public void testGetForValidUser() {
        try {
            String username = "paz.periasamy";
            User user = APIContextAware.getUserService().get(username);
            assertNotNull("User object not null.", user);
        } catch (APIException expAPI) {
            fail ("Do not expect APIException");
        }
    }
    
    
    @Test
    public void testUpdateForValidUser() {
        try {
            String username = "paz.periasamy";
            User user = APIContextAware.getUserService().get(username);
            assertNotNull("User object not null.", user);
            //user = APIContextAware.getUserService().update(user);            
        } catch (APIException expAPI) {
            fail ("Do not expect APIException");
        }
    }
    
    @Test
    public void testGetForInvalidUser() {
        String username = "firstname.lastname";
        try {
            User user = APIContextAware.getUserService().get(username);
            fail ("Should have failed.");
        } catch (APIException expAPI) {
            assertEquals("username does not exist.", "User [" + username + "] not found.", expAPI.getErrorMessage());
        }
    }
    
    @Test
    public void testList() {
        try {
            List <User> users = APIContextAware.getUserService().list();
            assertTrue("Users are empty.", (users.size() > 0));
        } catch (APIException e) {
            fail ("Do not expect APIException");
        }
    }

    private User getValidUser () {
        User user = new User ();
        user.setFirstName("Paz");
        user.setLastName("Periasamy");
        user.setEmail("paz.periasamy@oracle.com");
        Context context = new Context ();
        context.setName("University");
        context.addInterest("#math");
        context.addInterest("#physics");
        user.setContext(context);
        user.setPin("0000");
        return user;
    }
}

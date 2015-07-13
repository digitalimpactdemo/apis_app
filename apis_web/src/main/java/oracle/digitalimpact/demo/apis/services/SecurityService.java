package oracle.digitalimpact.demo.apis.services;

import java.io.Serializable;

import java.util.Collection;

import oracle.digitalimpact.demo.apis.exceptions.APIException;
import oracle.digitalimpact.demo.apis.model.Security;
import oracle.digitalimpact.demo.apis.model.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SecurityService implements UserDetailsService, Serializable {

    @SuppressWarnings("compatibility")
    private static final long serialVersionUID = 1L;
    
    private transient Log logger = LogFactory.getLog(this.getClass());
    
    private UserService userService = null;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Entered");
        try {
            Security security = getSecurityPrinicipal (username);
            Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList((String []) security.getRoles().toArray(new String[security.getRoles().size()]));
            return new org.springframework.security.core.userdetails.User (security.getUsername(), security.getPassword(), authorities);
        }
        catch (APIException expAPI) {
            throw new UsernameNotFoundException("Invalid username/password.");
        }
    }
    
    public Security authorise (String username) throws APIException {
        return getSecurityPrinicipal (username);
    }
    
    
    private Security getSecurityPrinicipal (String username) throws APIException {
        if (username != null) {
            username = username.trim();
        }
        User user = getUserService().getUserForAuthentication (username);
        Security security = new Security (user.getUsername());
        security.setPassword(user.getPin());
        security.addRole("ROLE_USER");
        return security;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserService getUserService() {
        return userService;
    }
}

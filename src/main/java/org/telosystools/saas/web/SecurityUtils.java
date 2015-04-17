package org.telosystools.saas.web;

/**
 * Created by Adrian on 17/04/15.
 *
 * Mocké pour le moment
 */
public class SecurityUtils {

    private static final String DEFAULT_USER = "user_default";

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     */
    public static String getCurrentLogin() {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        Authentication authentication = securityContext.getAuthentication();
//        UserDetails springSecurityUser = null;
//        String userName = null;
//        if(authentication != null) {
//            if (authentication.getPrincipal() instanceof UserDetails) {
//                springSecurityUser = (UserDetails) authentication.getPrincipal();
//                userName = springSecurityUser.getUsername();
//            } else if (authentication.getPrincipal() instanceof String) {
//                userName = (String) authentication.getPrincipal();
//            }
//        }
        return DEFAULT_USER;
    }
}

package se.stark.experiment.auth.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecurityService {
    public void authenticate(String principal) {
        SecurityContextHolder.clearContext();
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(
                principal,
                "",
                getAuthorities(principal));
        securityContext.setAuthentication(token);
        SecurityContextHolder.setContext(securityContext);

    }

    private List<SimpleGrantedAuthority> getAuthorities(String uid) {
        switch (uid) {
            case "admin":
                return List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN")
                );
            case "instructor":
                return List.of(
                        new SimpleGrantedAuthority("Instructor")
                );
            default:
                return List.of();
        }
    }

}

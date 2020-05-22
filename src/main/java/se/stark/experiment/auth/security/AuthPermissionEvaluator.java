package se.stark.experiment.auth.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class AuthPermissionEvaluator implements PermissionEvaluator {
    Logger log = LoggerFactory.getLogger(AuthPermissionEvaluator.class);

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permissionObject) {
        log.info("-- hasPermission --");
        Permission permission = Permission.valueOf(permissionObject.toString());
        boolean hasPermission = PersmissionService.hasPermission(permission);

        if (permission == Permission.ReadId23 ) {
            return hasPermission || !targetDomainObject.toString().equals("23");
        } else if (permission == Permission.ReadLargeId ) {
            return  hasPermission || ((Integer) targetDomainObject).intValue() < 10;
        } else if (permission == Permission.ReadSecretId ) {
            int id = ((Integer) targetDomainObject).intValue();
            boolean b = hasPermission || (id % 2) == 0;
            log.info("Authorized to read " + id + ": " + b);
            return b;
        }
        throw new IllegalStateException("No handler defined for permission " + permissionObject);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        log.info(String.format("hasPermission 2 %s %s", authentication, permission));
        return true;
    }

}

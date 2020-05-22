package se.stark.experiment.auth.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthAccessService {
    Logger logger = LoggerFactory.getLogger(AuthAccessService.class);

    public boolean hasUpdateUserPrivileges(Authentication authentication, String uid){
        logger.info("hasUpdateUserPrivileges uid:"+uid);
        logger.info("Checking privileges with an application bean inside a security expression");
        return authentication.getName().equals(uid);
    }

    public boolean hasGetIdPrivileges(int id) {
        logger.info("hasGetIdPrivileges id:"+id);
        logger.info("Checking privileges with an application bean inside a security expression");
        return true;
    }
}

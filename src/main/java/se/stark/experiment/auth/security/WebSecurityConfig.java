package se.stark.experiment.auth.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login/*").permitAll()
                .antMatchers("/home2").permitAll()
                .antMatchers("/logout").permitAll()
                .anyRequest().authenticated();
    }

/*
    @Bean
    PermissionEvaluator permissionEvaluator() {
        return new PermissionEvaluator() {
            Logger log = LoggerFactory.getLogger(PermissionEvaluator.class);

            @Override
            public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permissionObject) {
                log.info(String.format("hasPermission  %s %s", authentication, permissionObject));
                Permission permission = Permission.valueOf(permissionObject.toString());
                boolean hasPermission = PersmissionService.hasPermission(permission);

                if (permission == Permission.ReadId23 ) {
                    return hasPermission || !targetDomainObject.toString().equals("23");
                } else if (permission == Permission.ReadLargeId ) {
                    return  hasPermission || ((Integer) targetDomainObject).intValue() < 10;
                } else if (permission == Permission.ReadSecretId ) {
                    int id = ((Integer) targetDomainObject).intValue();
                    return hasPermission || (id % 2) == 0;
                }
                throw new IllegalStateException("No handler defined for permission " + permissionObject);
            }

            @Override
            public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
                log.info(String.format("hasPermission 2 %s %s", authentication, permission));
                return true;
            }
        };
    }
*/
}

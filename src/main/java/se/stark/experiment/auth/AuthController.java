package se.stark.experiment.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import se.stark.experiment.auth.security.AuthAccessService;
import se.stark.experiment.auth.security.PersmissionService;
import se.stark.experiment.auth.security.SecurityService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class AuthController {
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final IdService idService;
    private final SecurityService securityService;

    public AuthController(IdService idService, SecurityService securityService) {
        this.idService = idService;
        this.securityService = securityService;
    }

    @GetMapping("/login/{uid}")
    public RedirectView lti(@PathVariable("uid") String uid) {
        logger.info("login {}", uid);
        securityService.authenticate(uid);
        return new RedirectView("/home");
    }


    @GetMapping("/home")
    @ResponseBody
    public String home(HttpServletRequest req, Authentication authentication) {
        logger.info("home {}, {} (isAdmin: {})", authentication.getPrincipal(), authentication.getAuthorities(), req.isUserInRole("ROLE_ADMIN"));
        return String.format("username:%s, roles:%s permissions:%s ", authentication.getPrincipal(), authentication.getAuthorities(), PersmissionService.getPermissions());
    }

    @GetMapping("/home2/{id}")
    @ResponseBody
    @Secured("ROLE_ADMIN")
//    @PreAuthorize("hasPermission(#id, 'ReadId23')")
//    @PreAuthorize("hasAuthority('Instructor')")
//    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("isAuthenticated()")
//    @PreAuthorize("permitAll()")
    public String home2(@PathVariable("id") String id, Authentication authentication) {
        if (authentication != null) {
            logger.info("home2 {}, {}", authentication.getPrincipal(), authentication.getAuthorities());
        } else
            logger.info("home2 anonymous");
        return "I'm home again! " + id;
    }

    @GetMapping("/id/{id}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public Integer sid(@PathVariable("id") int id) {
        return idService.getId(id);
    }

    @GetMapping("/ids/{id}")
    @ResponseBody
    @PreAuthorize("isAuthenticated()")
    public List<Integer> sids(@PathVariable("id") int limit) {
        return idService.getIds(limit);
    }

    @GetMapping("/private/{uid}")
    @ResponseBody
    @PreAuthorize("#uid == authentication.name")
    public String private1(@PathVariable("uid") String uid) {
        return "Private information about me (" + uid + ") ";
    }

    @GetMapping("/private2/{uid}")
    @ResponseBody
    @PreAuthorize("@authAccessService.hasUpdateUserPrivileges(authentication, #uid)")
    public String private2(@PathVariable("uid") String uid) {
        return "Private information about me (" + uid + "), authorized by AccessService";
    }

    @GetMapping("/logout")
    @ResponseBody
    public String logout() {
        logger.info("logout");

        SecurityContextHolder.clearContext();
        return "logged out";
    }
}

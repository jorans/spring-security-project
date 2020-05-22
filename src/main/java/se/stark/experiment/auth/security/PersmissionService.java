package se.stark.experiment.auth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class PersmissionService {
    public static List<Permission> getPermissions() {
        ArrayList<Permission> permissions = new ArrayList<>();
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            for (Permission permission : getPermissions(authority)) {
                if (!permissions.contains(permission)) {
                    permissions.add(permission);
                }
            }
        }
        return permissions;
    }
    public static boolean hasPermission(Permission permission) {
        return getPermissions().contains(permission);
    }

    private static Collection<? extends Permission> getPermissions(GrantedAuthority authority) {
        switch (authority.getAuthority()) {
            case "ROLE_ADMIN":
                return List.of(
                        Permission.ReadId23,
                        Permission.ReadLargeId,
                        Permission.ReadSecretId
                        );
            case "Instructor":
                return List.of(
                        Permission.ReadId23,
                        Permission.ReadLargeId
                        );
            default:
                return List.of();
        }
    }
}

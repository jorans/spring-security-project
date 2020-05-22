package se.stark.experiment.auth;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import se.stark.experiment.auth.security.AuthAccessService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@PreAuthorize("denyAll()")
public class IdService {

    @PreAuthorize("permitAll()")
    @PostFilter("hasPermission(filterObject, 'ReadSecretId')")
    public List<Integer> getIds(int limit) {
        return IntStream.range(0, limit)
                .boxed()
                .collect(Collectors.toList());

    }

    @PreAuthorize("@authAccessService.hasGetIdPrivileges(#id)")
    public Integer getId(int id) {
        return id;
    }

}

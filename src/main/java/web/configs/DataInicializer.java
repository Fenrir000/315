package web.configs;

import org.springframework.stereotype.Component;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInicializer {
    private final UserService userService;
    private final RoleService roleService;


    public DataInicializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void initialize() {
        roleService.saveRole (new Role("ROLE_ADMIN"));
        roleService.saveRole(new Role("ROLE_USER"));
        Set<Role> adminRole = new HashSet<>();
        Set<Role> userRole = new HashSet<>();
       adminRole.add(roleService.findRoleByRoleName("ROLE_ADMIN"));
       userRole.add(roleService.findRoleByRoleName("ROLE_USER"));
        userService.save(new User("a@a.ru", "Sdasd", "pass", "asmin",  adminRole));
        userService.save(new User("u@u.ru", "Asdasdas", "pass", "asd",  userRole));


    }
}

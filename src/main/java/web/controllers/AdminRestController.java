package web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin")
public class AdminRestController {
    private final UserService userService;
    private final RoleService roleService;


    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRole() {
        return new ResponseEntity<>(roleService.findAllRoles(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getOneUser(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(userService.findById(id).get(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<User> creatRestUser(@RequestBody User user) {
        List<String> convert = user.getRoles().stream().map(Role::getRole).collect(Collectors.toList());
        List<Role> newRoles = roleService.listByRole(convert);
        if (newRoles.isEmpty()) {
            user.setRoles(Set.of(roleService.findRoleByRoleName("ROLE_USER")));
        } else {
            user.setRoles(Set.copyOf(newRoles));
        }
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<User> updateRestUser(@RequestBody User user) {
        if (user.getRoles().isEmpty()) {
            user.setRoles(userService.findById(user.getId()).get().getRoles());
        } else {
            List<String> convert = user.getRoles().stream().map(Role::getRole).collect(Collectors.toList());
            List<Role> newRoles = roleService.listByRole(convert);
            user.setRoles(Set.copyOf(newRoles));
        }
        userService.updateUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteRestUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}

package web.service;


import web.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAllRoles();

    void saveRole(Role role);

    List<Role> findRoleByName(String roleName);

    Role findRoleById(Long id);

    Role findRoleByRoleName(String roleName);

    List<Role> listByRole(List<String> names);
}
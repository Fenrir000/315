package web.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.model.Role;
import web.repository.RoleDAO;


import java.util.ArrayList;
import java.util.List;

@Service

public class RoleServiceImpl implements RoleService {

    private final RoleDAO roleDAO;


    public RoleServiceImpl(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findAllRoles() {
        return roleDAO.findAllRole();
    }

    @Override
    @Transactional
    public void saveRole(Role role) {
        roleDAO.saveRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> findRoleByName(String roleName) {
        return roleDAO.findRole(roleName);
    }

    @Override
    @Transactional(readOnly = true)
    public Role findRoleById(Long id) {
        return roleDAO.findRoleById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Role findRoleByRoleName(String roleName) {
        return roleDAO.findRoleByRoleName(roleName);
    }

    @Transactional(readOnly = true)
    public List<Role> listByRole(List<String> names) {
        List<Role> roles = new ArrayList<>();
        List<Role> listRoles = roleDAO.findAllRole();
        for (String name : names) {
            listRoles.stream().filter(role -> role.getRole().contains(name)).forEach(roles::add);
        }
        return roles;
    }
}

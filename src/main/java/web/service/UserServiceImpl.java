package web.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.model.Role;
import web.model.User;
import web.repository.UserDAO;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final PasswordEncoder encoder;
    private final UserDAO userDao;
    private final RoleService roleService;

    public UserServiceImpl(PasswordEncoder encoder, UserDAO userDao, RoleService roleService) {
        this.encoder = encoder;
        this.userDao = userDao;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public void save(User user) {
        List<String> convert = user.getRoles().stream().map(Role::getRole).collect(Collectors.toList());
        List<Role> newRoles = roleService.listByRole(convert);
        if(newRoles.isEmpty()){
            user.setRoles(Set.of(roleService.findRoleByRoleName("ROLE_USER")));
        }else{
        user.setRoles(Set.copyOf(newRoles));}
        user.setPassword(encoder.encode(user.getPassword()));
        userDao.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        User oldUser = userDao.findById(user.getId()).get();
        if(!user.getPassword().equals(oldUser.getPassword())) {
            user.setPassword(encoder.encode(user.getPassword()));
        }

        if (user.getRoles().isEmpty()) {
            user.setRoles(oldUser.getRoles());
        } else {
            List<String> convert = user.getRoles().stream().map(Role::getRole).collect(Collectors.toList());
            List<Role> newRoles = roleService.listByRole(convert);
            user.setRoles(Set.copyOf(newRoles));
        }

        userDao.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Not found"));


    }

}
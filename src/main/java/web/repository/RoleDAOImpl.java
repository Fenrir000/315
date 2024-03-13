package web.repository;

import org.springframework.stereotype.Repository;
import web.model.Role;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class RoleDAOImpl implements RoleDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role findRoleByRoleName(String roleName) {
        String jpql = "SELECT r FROM Role r WHERE r.role = :roleName";
        TypedQuery<Role> query = entityManager.createQuery(jpql, Role.class);
        query.setParameter("roleName", roleName);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Role> findAllRole() {
        String jpql = "SELECT r FROM Role r";
        TypedQuery<Role> query = entityManager.createQuery(jpql, Role.class);
        return query.getResultList();
    }

    @Override
    public void saveRole(Role role) {
        entityManager.persist(role);

    }

    @Override
    public Role findRoleById(Long id) {
        return entityManager.find(Role.class, id);
    }


    @Override
    public List<Role> findRole(String roleName) {
        String jpql = "SELECT r FROM Role r WHERE r.role = :roleName";
        TypedQuery<Role> query = entityManager.createQuery(jpql, Role.class);
        query.setParameter("roleName", roleName);
        return query.getResultList();
    }
}

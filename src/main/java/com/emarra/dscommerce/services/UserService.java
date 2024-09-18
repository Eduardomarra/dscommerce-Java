package com.emarra.dscommerce.services;

import com.emarra.dscommerce.entities.Role;
import com.emarra.dscommerce.entities.User;
import com.emarra.dscommerce.projections.UserDetailsProjection;
import com.emarra.dscommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(userName);
        if(result.isEmpty()) throw new UsernameNotFoundException("User not found");

        User user = new User();
        user.setEmail(userName);
        user.setPassword(result.get(0).getPassword());

        for(UserDetailsProjection projection: result) {
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        return user;
    }
}

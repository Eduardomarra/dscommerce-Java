package com.emarra.dscommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emarra.dscommerce.dto.UserDTO;
import com.emarra.dscommerce.entities.Role;
import com.emarra.dscommerce.entities.User;
import com.emarra.dscommerce.projections.UserDetailsProjection;
import com.emarra.dscommerce.repositories.UserRepository;

@Service
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
    
    protected User authenticated() {
    	try {
    		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        	Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
        	String username = jwtPrincipal.getClaim("username");

        	return repository.findByEmail(username).get();
    	} 
    	catch (Exception e) {
    		throw new UsernameNotFoundException("Email não encontrado.");
    	}
    }
    
    @Transactional(readOnly = true)
    public UserDTO getUserLogged() {
    	User user = authenticated();
    	return  new UserDTO(user);
    }
}

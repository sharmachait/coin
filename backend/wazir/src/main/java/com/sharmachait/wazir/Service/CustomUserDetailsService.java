package com.sharmachait.wazir.Service;

import com.sharmachait.wazir.Repository.IUserRepository;
import com.sharmachait.wazir.Model.Entity.WazirUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//registering a bean of a service that implements the UserDetailsService
// tells the spring security framework to not use the default in memory user manager
//and use this instead, and therefore doesnt create a user at startup
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WazirUser wazirUser = userRepository.findByEmail(username);
        if(wazirUser ==null)
            throw new UsernameNotFoundException(username);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(wazirUser.getRole()+""));
        return new User(wazirUser.getEmail(), wazirUser.getPassword(),authorities);
    }
}

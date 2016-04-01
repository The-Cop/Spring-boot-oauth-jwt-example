package com.thecop.springoauthjwt.service.impl;

import com.thecop.springoauthjwt.service.ISimpleUserDetailsService;
import com.thecop.springoauthjwt.user.CustomUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
public class SimpleUserDetailsService implements ISimpleUserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleUserDetailsService.class);
    private final static Map<String, CustomUser> users = new HashMap<String, CustomUser>();

    static {
        CustomUser u = new CustomUser("user", "user", "User full name");
        u.setAuthorities(new HashSet<>(
                Arrays.asList(new SimpleGrantedAuthority[]{new SimpleGrantedAuthority("ROLE_USER")})));
        users.put(u.getUsername(), u);

        CustomUser a = new CustomUser("admin", "admin", "Admin full name");
        a.setAuthorities(new HashSet<>(
                Arrays.asList(new SimpleGrantedAuthority[]{
                        new SimpleGrantedAuthority("ROLE_USER"), new SimpleGrantedAuthority("ROLE_ADMIN")})));
        users.put(a.getUsername(), a);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return users.get(login);
    }

    @Override
    public Iterable<CustomUser> getUsers() {
        return users.values();
    }
}

package com.thecop.springoauthjwt.service;

import com.thecop.springoauthjwt.user.CustomUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ISimpleUserDetailsService extends UserDetailsService {

    Iterable<CustomUser> getUsers();
}

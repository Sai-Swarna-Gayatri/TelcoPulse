package com.ssg.telcopulse.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.ssg.telcopulse.model.AppUser;
import com.ssg.telcopulse.repository.AppUserRepository;

@Service 
public class CustomUserDetailsService implements UserDetailsService{

    private final AppUserRepository appUserRepository;

    private final static Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    public CustomUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

       AppUser appUser = appUserRepository.findByUserName(username)
       .orElseThrow(()->new UsernameNotFoundException("User not found with userName "+ username));

       UserDetails user = User.withUsername(appUser.getUserName())
                .password(appUser.getPassword())
                .roles(appUser.getRole().toString())
                .build();

        return user;
    }
}

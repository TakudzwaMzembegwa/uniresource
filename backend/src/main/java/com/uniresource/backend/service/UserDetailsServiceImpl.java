package com.uniresource.backend.service;

import java.util.ArrayList;

import com.uniresource.backend.domain.entity.User;
import com.uniresource.backend.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user;
        if (usernameOrEmail.contains("."))
            user = userRepository.findByEmail(usernameOrEmail).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        else
            user = userRepository.findByUsername(usernameOrEmail).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        if (user == null)
            throw new UsernameNotFoundException(usernameOrEmail);
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
         user.getPassword(), new ArrayList<>());
    }
}

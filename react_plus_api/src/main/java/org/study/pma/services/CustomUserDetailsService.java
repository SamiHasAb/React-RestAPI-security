package org.study.pma.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.pma.models.User;
import org.study.pma.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRep;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRep.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("User not found");
        return user;
    }


    @Transactional
    public User loadUserById(Long id) {
        User user = userRep.getById(id);
        if (user == null) throw new UsernameNotFoundException("User not found");
        return user;

    }
}
package com.twojprojekt.tanibilet.security;

import com.twojprojekt.tanibilet.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.twojprojekt.tanibilet.model.User;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

//    @Override
//    public UserDetails loadUserByUsername(String username){
//        return repo.findByUsername(username)
//                .map(u -> User.builder()
//                        .username(u.getUsername())
//                        .password(u.getPassword())
//                        .roles(u.getRoles().toArray(new String[0]))
//                        .build())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//    }
@Override
public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException {

    // 1) Pobierz swoją encję User
    User appUser = repo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    // 2) Mapa ról (String) na GrantedAuthority
    List<GrantedAuthority> authorities = appUser.getRoles().stream()
            .map(roleName -> {
                // jeśli rola nie ma już prefixu, dopisz ROLE_
                String name = roleName.startsWith("ROLE_")
                        ? roleName
                        : "ROLE_" + roleName;
                return new SimpleGrantedAuthority(name);
            })
            .collect(Collectors.toList());

    // 3) Zbuduj obiekt UserDetails Springa
    return org.springframework.security.core.userdetails.User.builder()
            .username(appUser.getUsername())
            .password(appUser.getPassword())
            .authorities(authorities)
            .build();
}



}






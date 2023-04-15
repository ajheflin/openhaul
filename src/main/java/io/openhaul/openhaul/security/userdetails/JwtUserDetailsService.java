package io.openhaul.openhaul.security.userdetails;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JwtUserDetailsService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if("user".equals(username)) {
            return new User("user", "user", Stream.of("ROLE_USER").map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        } else if("admin".equals(username)) {
            return new User("admin", "admin", Stream.of("ROLE_ADMIN").map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}

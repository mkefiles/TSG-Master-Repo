package net.yorksolutions.service;

import net.yorksolutions.entity.Users;
import net.yorksolutions.entity.UsersPrincipal;
import net.yorksolutions.repository.UsersRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// NOTE: Based on research, a separate AuthenticationProvider bean, in the
// ... SecurityConfig file, is NOT required when a custom UserDetailsService
// ... 'bean' is created. That is because Spring Security auto-configures
// ... a DaoAuthenticationProvider for you, which automatically picks up
// ... your defined UserDetailsService. That said, the DaoAuthenticationProvider
// ... does require a PasswordEncoder to be configured unless you are using a
// ... NoOpPasswordEncoder (i.e., plain-text). See SecurityConfig for a
// ... PasswordEncoder bean
@Service
public class CustomUserDetailsService implements UserDetailsService {
    // DESC: Inject the following dependencies for Spring Context
    // ... to auto-wire upon initialization
    private final UsersRepository usersRepository;

    // DESC: Constructor-method to initialize all necessary dependencies
    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    // DESC: This retrieves the `Users` details and adapts them into the
    // ... Spring Security's `User` format
    // NOTE: Due to our table structure not having an 'authorities' table
    // ... (to specify the role) or an 'enabled' column in 'users', the
    // ... logic needs to override defaults
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DESC: Locate a User by their email address (not a username)
        Users users = usersRepository.findByEmailIgnoreCase(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));

        // DESC: Load the authority/ies into a list
        // NOTE: With this example, each user will only have ONE role, so a list is
        // ... not entirely necessary, however it wont hurt to have it
        // NOTE: The `.name()` method is what converts an ENUM value to a String
//        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(users.getRole().name()));

//        return new User(users.getEmail(), users.getPassword_hash(), authorities);
        return new UsersPrincipal(users);
    }
}

package net.yorksolutions.security;

import jakarta.validation.groups.Default;
import net.yorksolutions.filter.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    // DESC: Inject the following dependencies for Spring Context
    // ... to auto-wire upon initialization
    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    // DESC: Constructor-method to initialize all necessary dependencies
    public SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    // DESC: Alter the End-Points (what should/not be behind a login-wall)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // DESC: Specify who can access what end-points
        // NOTE: `endpoint_auth` is a parameter of a Lambda Expression as such
        // ... the name is irrelevant
        // NOTE: In order, the end-points are auth.-blocked as follows:
        // - Anyone can access either Plans end-points IF the method is GET
        // - Anyone can access either Auth end-points IF the method is POST
        // - Only a Member can access an end-point that starts with '/me'
        // - Only an Admin can access an end-point that starts with '/admin'
        http.authorizeHttpRequests(endpoint_auth -> endpoint_auth
                .requestMatchers(HttpMethod.POST, "/api/v1/plans").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/plans").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/plans/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/me/**").authenticated()
                .requestMatchers("/api/v1/admin/**").authenticated()
                .anyRequest().denyAll()
        );

        // DESC: Enable REST-access to APIs (i.e., use a Front-End framework to
        // ... to consume the API end-points)
        http.httpBasic(Customizer.withDefaults());

        // DESC: Disable the Cross Site Request Forgery (CSRF)
        // NOTE: Being that the back-end will be using a token-based authentication
        // ... that will be passed back and forth with the front-end, the likelihood
        // ... of a successful CSRF attack being carried out is nil so this is being
        // ... disabled
        http.csrf(csrf -> csrf.disable());

        // DESC: Make the Spring Security stateless
        // NOTE: This is a requirement of the project
        // NOTE: This is a step towards passing a token on each request (once logged
        // ... in) that way there is no need for session-management on the server-side
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // DESC: Specify the 'filter' to use
        // NOTE: This is read as, before using the Username/Password filter
        // ... check the JWT filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // DESC: Handle the Argon2 Password Encoding
    // NOTE: The Argon2 parameters (i.e., salt-length, memory, etc.)
    // ... can be customized
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder(32, 64, 1, 65536, 2);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // DESC: ...
    // NOTE: The following code is necessary to implement JWT
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // DESC: This overrides the default behavior of the
    // ... @PreAuthorize("hasRole()") annotation
    // NOTE: By default, "ROLE_" should be added to the 'role'
    // ... in the database as such Spring will check for a role
    // ... with "ROLE_" (e.g., "ROLE_MEMBER") and, because the
    // ... current table-build does not have that, it was failing
    // ... the PreAuthorize
    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
        defaultMethodSecurityExpressionHandler.setDefaultRolePrefix("");
        return defaultMethodSecurityExpressionHandler;
    }

}

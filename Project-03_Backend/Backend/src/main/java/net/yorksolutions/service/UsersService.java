package net.yorksolutions.service;

import net.yorksolutions.entity.Members;
import net.yorksolutions.entity.Users;
import net.yorksolutions.repository.MembersRepository;
import net.yorksolutions.repository.UsersRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UsersService {
    // DESC: Inject the following dependencies for Spring Context
    // ... to auto-wire upon initialization
    private final MembersRepository membersRepository;
    private final UsersRepository usersRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    // DESC: Constructor-method to initialize all necessary dependencies
    public UsersService(
            MembersRepository membersRepository,
            UsersRepository usersRepository,  AuthenticationManager authenticationManager,
            JWTService jwtService
    ) {
        this.membersRepository = membersRepository;
        this.usersRepository = usersRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // PT04 - Service - For createNewMemberUser()
    // HTTP [Method - URL]: POST - localhost:8080/api/v1/auth/register
    @Transactional
    public int createNewMemberUser(Users newUser, Members newMember) {
        // DESC: Ensure email is not blank
        if (newUser.getEmail().isEmpty()) {
            return 400;
        } else {
            // DESC: Ensure password is greater-than OR equal-to four characters
            if (newUser.getPasswordHash().length() < 4) {
                return 400;
            } else {
                // DESC: Ensure username is not duplicate
                if (usersRepository.existsByEmailIgnoreCase(newUser.getEmail())) {
                    return 409;
                } else {
                    // DESC: Add User to database and update ID based on return-data
                    Users savedUser = usersRepository.save(newUser);
                    UUID savedUserId = savedUser.getId();
                    newUser.setId(savedUserId);

                    // DESC: Add Member to database
                    // NOTE: Due to Hibernate error, this does not currently contain
                    // ... the linking User ID. Based on the layout of the Data Model
                    // ... I do not believe I can make this work to the Data JPA
                    // ... expected layout so I am using this multi-step process as
                    // ... a work-around
                    Members savedMember = membersRepository.save(newMember);
                    UUID savedMemberId = savedMember.getId();

                    // DESC: Run a Custom Query handling an Update
                    // NOTE: When updating a database with Spring Data JPA, the
                    // ... `save()` method will, supposedly, overwrite all columns
                    // ... with the details in Entity Object. This method, below,
                    // ... relies on a Custom Query that updates ONE column and,
                    // ... thus, should run more efficiently
                    membersRepository.updateMembers(savedUserId, savedMemberId);

                    return 200;
                }
            }
        }
    }

    public int createNewAdminUser(Users newUser) {
        // DESC: Add admin to database
        // NOTE: No precautions were taken here because I am creating
        // ... this logic solely so I can have a proper admin-entry
        // ... in the database in lieu of some hand-coded one (i.e.,
        // ... it will not be open for general access)
        usersRepository.save(newUser);
        return 200;
    }

    public String verifyUserCredentials(Users users) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                users.getEmail(),
                                users.getPasswordHash()
                        )
                );

        if (authentication.isAuthenticated()) {

            return jwtService.generateToken(users.getEmail());
        } else {
            return "Failed";
        }
    }

    public Users getUserRoleAndId(Users users) {
        return usersRepository.findByEmailIgnoreCase(users.getEmail())
                .orElseThrow(
                        () -> new NoSuchElementException()
                );
    }

}
package com.example.controller;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.Arrays;
import java.util.List;


// Step 000: Annotation-based Configuration
// Step 01A: Identify a Controller (Annotation)
// ... @RestController removes need for method-annotation @ResponseBody
@RestController // Controller "Bean"
public class SocialMediaController {

    private MessageService messageService;
    private AccountService accountService;

    public SocialMediaController(MessageService messageService, AccountService accountService) {
        this.messageService = messageService;
        this.accountService = accountService;
    }

    // CLI to Output Beans (OPTIONAL; CAN BE REMOVED)
    /*
    @Bean
    public CommandLineRunner getAllBeans(ApplicationContext applicationContext) {
        return args -> {
            System.out.println("INITIATE: FETCH BEANS");
            String[] beans = applicationContext.getBeanDefinitionNames();
            Arrays.sort(beans);
            for(String bean : beans) {
                System.out.println("BEAN NAME: " + bean);
            }
            System.out.println("TERMINATE: FETCH BEANS");
        };
    }
    */

    // PT01 -- Handler for User Registration (POST - localhost:8080/register)
    @PostMapping("/register")
    public ResponseEntity<Account> createNewAccount(@RequestBody Account newAccount) {
        // DESC: Call the AccountService (for logic)
        int returnStatusCode = accountService.createNewAccount(newAccount);

        // DESC: Determine 'return'
        switch (returnStatusCode) {
            case 200:
                // DESC: A successful registration (i.e., username and password passed requirements)
                // Use `.body()` to return JSON of Account object
                return ResponseEntity
                        .status(200)
                        .body(newAccount);
            case 409:
                // DESC: A failed registration (due to a duplicate username)
                // Use `.build()` to return an empty body
                return ResponseEntity
                        .status(409)
                        .build();
            default:
                // DESC: A failed registration (will always return code 400 for all other issues)
                // Use `.build()` to return an empty body
                return ResponseEntity
                        .status(400)
                        .build();
        }
    }

    // PT02 -- Handler for User Login (POST - localhost:8080/login)
    @PostMapping("/login")
    public ResponseEntity<Account> readAccountToLogin(@RequestBody Account existingAccount) {
        // DESC: Call the AccountService (for logic)
        int returnStatusCode = accountService.readAccountToLogin(existingAccount);

        switch (returnStatusCode) {
            case 200:
                // DESC: A successful login (i.e., username and password match existing account)
                // Use `.body()` to return JSON of Account object
                return ResponseEntity
                        .status(200)
                        .body(existingAccount);
            default:
                // DESC: A failed login (username and/or password incorrect)
                // Use `.build()` to return an empty body
                return ResponseEntity
                        .status(401)
                        .build();
        }
    }

    // PT03 -- Handler for New Message (POST - localhost:8080/messages)
    @PostMapping("/messages")
    public ResponseEntity<Message> createNewMessage(@RequestBody Message newMessage) {
        // DESC: Call the MessageService (for logic)
        int returnStatusCode = messageService.createNewMessage(newMessage);

        switch (returnStatusCode) {
            case 200:
                // DESC: A successful message creation
                // Use `.body()` to return JSON of Message object
                return ResponseEntity
                        .status(200)
                        .body(newMessage);
            default:
                // DESC: A failed message creation (a catch-all)
                // Use `.build()` to return an empty body
                return ResponseEntity
                        .status(400)
                        .build();
        }
    }

    // PT04 -- Handler for Getting Messages [All] (GET - localhost:8080/messages)
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> readAllMessages() {
        // DESC: Return all messages; Always have response of 200
        return ResponseEntity
                .status(200)
                .body(messageService.readAllMessages());
    }

    // PT05 -- Handler for Getting Messages [By ID] (GET - localhost:8080/messages/{message_id})
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> readMessageByID(@PathVariable String messageId) {
        // DESC: Convert String parameter to integer
        int messageIdInt = Integer.parseInt(messageId);

        // DESC: Return message by ID; Always have response of 200
        List<Message> results = messageService.readMessageByID(messageIdInt);

        // DESC: Determine is 'results' List is empty
        if (results.isEmpty()) {
            // DESC: List is empty; Return empty body
            return ResponseEntity
                    .status(200)
                    .build();
        } else {
            // DESC: List contains result(s); Return result(s)
            return ResponseEntity
                    .status(200)
                    .body(results.get(0));
        }
    }

    // PT06 -- Handler for Deleting Message [By ID] (DELETE - localhost:8080/messages/{message_id})
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageByID(@PathVariable String messageId) {
        // DESC: Convert String parameter to integer
        int messageIdInt = Integer.parseInt(messageId);

        // DESC: Call the MessageService (for logic)
        int updatedRowCount = messageService.deleteMessageByID(messageIdInt);

        switch (updatedRowCount) {
            case 1:
                // DESC: A successful deletion
                // Use `.body()` to return row(s) deleted (should always be '1')
                return ResponseEntity
                        .status(200)
                        .body(updatedRowCount);
            default:
                // DESC: A failed deletion (i.e., ID not found)
                // Use `.build()` to return an empty body
                return ResponseEntity
                        .status(200)
                        .build();
        }
    }


    // PT07 -- Handler for Updating Message [By ID] (PATCH - localhost:8080/messages/{message_id})
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessageByID(@RequestBody Message updatedMessage, @PathVariable String messageId) {
        // DESC: Convert String parameter to integer
        int messageIdInt = Integer.parseInt(messageId);

        // DESC: Call the MessageService (for logic)
        int updatedRowCount = messageService.updateMessageByID(messageIdInt, updatedMessage);

        switch (updatedRowCount) {
            case 1:
                // DESC: A successful update
                // Use `.body()` to return row(s) updated (should always be '1')
                return ResponseEntity
                        .status(200)
                        .body(updatedRowCount);
            default:
                // DESC: A failed update (catch-all for length, message id doesn't exist, etc.)
                // Use `.build()` to return an empty body
                return ResponseEntity
                        .status(400)
                        .build();
        }


    }

    // PT08 -- Handler for Getting Messages [By User] (GET - localhost:8080/accounts/{account_id}/messages)
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> readAllMessagesByAccountID(@PathVariable String accountId) {
        // DESC: Convert String parameter to integer
        int accountIdInt = Integer.parseInt(accountId);

        // DESC: Return all messages; Always have response of 200
        return ResponseEntity
                .status(200)
                .body(messageService.readAllMessagesByAccountID(accountIdInt));
    }
}

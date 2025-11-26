package com.example.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Step 000: Annotation-based Configuration
// Step 01A: Identify a Controller (Annotation)
@Service // Service "Bean"
public class MessageService {

    private Message message = new Message();

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    AccountRepository accountRepository;

    // PT03 -- Service for New Message (POST - localhost:8080/messages)
    public int createNewMessage(Message newMessage) {
        // DESC: Ensure message meets length requirements
        int messageLength = newMessage.getMessageText().length();
        if (messageLength == 0 || messageLength > 255) {
            // DESC: Message failed to meet length requirements
            return 400;
        } else {
            // DESC: Ensure postedBy ID, of message, is found as ID in Account
            if (accountRepository.existsById(newMessage.getPostedBy())) {
                // DESC: Successful match on ID
                // DESC: Add message to database
                messageRepository.save(newMessage);

                // DESC: Get new message ID from database and set to newMessage instance
                List<Message> messageEntries = messageRepository.findByMessageText(newMessage.getMessageText());
                for (Message messageEntry : messageEntries) {
                    if (messageEntry.getMessageText().equals(newMessage.getMessageText())) {
                        newMessage.setMessageId(messageEntry.getMessageId());
                    }
                }

                // DESC: Message creation is a success
                return 200;
            } else {
                // DESC: No ID match
                return 400;
            }
        }
    }

    // PT04 -- Service for Getting Messages [All] (GET - localhost:8080/messages)
    public List<Message> readAllMessages() {
        return messageRepository.findAll();
    }

    // PT05 -- Service for Getting Messages [By ID] (GET - localhost:8080/messages/{message_id})
    public List<Message> readMessageByID(int messageIdInt) {
        // DESC: Get by ID; Use Optional in event that nothing is there
        Optional<Message> messageById = messageRepository.findById(messageIdInt);

        // DESC: Create a List to hold message (if applicable)
        List<Message> messageEntries = new ArrayList<>();

        // DESC: Adds message from Optional to List (only if it exists)
        messageById.ifPresent(messageEntries::add);

        return messageEntries;
    }

    // PT06 -- Service for Deleting Message [By ID] (DELETE - localhost:8080/messages/{message_id})
    public int deleteMessageByID(int messageIdInt) {
        // DESC: Ensure message exists in database
        if (messageRepository.existsById(messageIdInt)) {
            // DESC: Delete message by ID, then return a hard-coded one (for success)
            messageRepository.deleteById(messageIdInt);
            return 1;
        } else {
            // DESC: Message not found based on provided ID
            return 0;
        }
    }

    // PT07 -- Service for Updating Message [By ID] (PATCH - localhost:8080/messages/{message_id})
    public int updateMessageByID(int messageIdInt, Message updatedMessage) {
        // DESC: Ensure message meets length requirements
        int messageLength = updatedMessage.getMessageText().length();
        if (messageLength == 0 || messageLength > 255) {
            // DESC: Message failed to meet length requirements
            return 0;
        } else {
            // DESC: Ensure message ID is found
            if (messageRepository.existsById(messageIdInt)) {
                // DESC: Get stored message values (to ensure nothing is blank on update)
                Optional<Message> tempMessageOpt = messageRepository.findById(messageIdInt);

                // DESC: Create empty list (to tranfer Optional)
                List<Message> tempMessageEntries = new ArrayList<>();

                // DESC: Transfer value of Optional to list
                tempMessageOpt.ifPresent(tempMessageEntries::add);

                // DESC: Ensure updatedMessage instance contains URI-passed ID
                updatedMessage.setMessageId(messageIdInt);

                // DESC: Ensure postedBy ID is either user-provided OR what is in the database (not empty)
                updatedMessage.setPostedBy((updatedMessage.getPostedBy() != null) ?
                        updatedMessage.getPostedBy() : tempMessageEntries.get(0).getPostedBy());

                // DESC: Ensure timePostedEpoch is either user-provided OR what is in the database (not empty)
                updatedMessage.setTimePostedEpoch((updatedMessage.getTimePostedEpoch() != null) ?
                        updatedMessage.getTimePostedEpoch() : tempMessageEntries.get(0).getTimePostedEpoch());

                // DESC: Message exists in database, proceed to update
                // `.save()` will either add (if it does not exist) OR it updates
                messageRepository.save(updatedMessage);

                // DESC: Return a hard-coded one (for success)
                return 1;
            }

            // DESC: Message does not exist, by ID, in database
            return 0;
        }
    }

    // PT08 -- Service for Getting Messages [By User] (GET - localhost:8080/accounts/{account_id}/messages)
    public List<Message> readAllMessagesByAccountID(int accountIdInt) {
        return messageRepository.findByPostedBy(accountIdInt);
    }
}

package com.example.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    // PT03 -- D.B. C.R.U.D. for New Message (POST - localhost:8080/messages)
    public List<Message> findByMessageText(String messageText);

    // PT04 -- D.B. C.R.U.D. for Getting Messages [All] (GET - localhost:8080/messages)
    // Handled by Spring Data JPA (Built-In Method(s))

    // PT05 -- D.B. C.R.U.D. for Getting Messages [By ID] (GET - localhost:8080/messages/{message_id})
    // Handled by Spring Data JPA (Built-In Method(s))

    // PT06 -- D.B. C.R.U.D. for Deleting Message [By ID] (DELETE - localhost:8080/messages/{message_id})
    // Handled by Spring Data JPA (Built-In Method(s))

    // PT07 -- D.B. C.R.U.D. for Updating Message [By ID] (PATCH - localhost:8080/messages/{message_id})
    // Handled by Spring Data JPA (Built-In Method(s))

    // PT08 -- D.B. C.R.U.D. for Getting Messages [By User] (GET - localhost:8080/accounts/{account_id}/messages)
    public List<Message> findByPostedBy(Integer postedBy);
}

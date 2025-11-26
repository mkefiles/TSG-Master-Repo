package net.yorksolutions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@SpringBootApplication

// This is the class that is used to run the application.
// NOTHING HERE NEEDS TO BE MODIFIED
public class BackendApp {

    /**
     * Run the application
     * @param args the arguments of the program
     */
	public static void main(String[] args) {
        SpringApplication.run(BackendApp.class, args);
	}

}

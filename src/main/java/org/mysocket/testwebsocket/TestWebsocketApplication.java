package org.mysocket.testwebsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class TestWebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestWebsocketApplication.class, args);
    }

}

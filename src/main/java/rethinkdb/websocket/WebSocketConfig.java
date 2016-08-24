package rethinkdb.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created by Nicolas on 8/21/2016.
 *
 * Now that we can read and write from the DB, we need to push the updates to the client in real time.
 * We will use websockets over SockJS for that. The configuration for websockets is pretty classic.
 */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chatWS").withSockJS();
    }
}

/**
 * How to read that:
 *
 * Our clients will be able to connect to the /chatWS endpoint.
 * The clients will then have the possibility to listen to any topic whose url begins with /topic
 * (i.e, /topic/messages) and get notified in real time.
 */

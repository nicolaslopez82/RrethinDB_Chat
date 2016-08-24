package rethinkdb.db;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Nicolas on 8/21/2016.
 */

@Configuration
public class RethinkDBConfiguration {

    // connect to docker
    public static final String DBHOST = "192.168.99.100";

    @Bean
    public RethinkDBConnectionFactory connectionFactory(){
        return new RethinkDBConnectionFactory(DBHOST);
    }

    @Bean
    DbInitializer dbInitializer(){
        return new DbInitializer();
    }
}

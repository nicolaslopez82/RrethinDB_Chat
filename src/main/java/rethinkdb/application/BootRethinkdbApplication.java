package rethinkdb.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Created by Nicolas on 8/21/2016.
 */

@SpringBootApplication
@EnableAutoConfiguration
@EnableAsync
public class BootRethinkdbApplication {

    public static void main(String[] args){
        SpringApplication.run(BootRethinkdbApplication.class, args);
    }
}

package rethinkdb.chat;


import com.rethinkdb.RethinkDB;
import rethinkdb.db.RethinkDBConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeoutException;

import static java.time.OffsetDateTime.now;


/**
 * Created by Nicolas on 8/21/2016.
 *
 * The ChatController
 * The rethinkdb.chat controller will react to two things:
 * GETting the last 20 messages from the DB.
 * POSTing a new message.
 *
 * Some things are still a bit funny:
 * The optArg after the orderBy is a bit cryptic
 * The POJO class must not contain any id attribute for the auto-generation to work for RethinkDB.
 *
 */

@RestController
@RequestMapping("/rethinkdb/chat")
public class ChatController {

    protected final Logger log = LoggerFactory.getLogger(ChatController.class);
    private static final RethinkDB r = RethinkDB.r;

    @Autowired
    private RethinkDBConnectionFactory connectionFactory;

    @RequestMapping(method = RequestMethod.POST)
    public ChatMessage postMessage(@RequestBody ChatMessage chatMessage){
        chatMessage.setTime(now());
        Object run = null;
        try {
            run = r.db("rethinkdb/chat").table("message").insert(chatMessage).run(connectionFactory.createConnection());
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        log.info("Insert {}", run);
        return chatMessage;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ChatMessage> getMessages(){

        List<ChatMessage> messages = null;
        try {
            messages = r.db("rethinkdb/chat").table("messages")
                    .orderBy().optArg("index", r.desc("time"))
                    .limit(20)
                    .orderBy("time")
                    .run(connectionFactory.createConnection(), ChatMessage.class);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return messages;
    }
}

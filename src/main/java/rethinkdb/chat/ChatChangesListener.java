package rethinkdb.chat;

import rethinkdb.db.RethinkDBConnectionFactory;
import org.slf4j.Logger;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.TimeoutException;


/**
 * Created by Nicolas on 8/21/2016.
 *
 * We will now listen to database updates in a thread and broadcast the changes to all the clients listening on the web socket.
 * We use the @Async annotation, so Spring will take care of running the code in a thread for us.
 *
 */


public class ChatChangesListener {

    protected final Logger log = org.slf4j.LoggerFactory.getLogger(ChatMessage.class);
    private static final RethinkDB r = RethinkDB.r;

    @Autowired
    private RethinkDBConnectionFactory connectionFactory;

    @Autowired
    private SimpMessagingTemplate webSocket;

    @Async
    public void pushChangesToWebSocket(){
        Cursor<ChatMessage> cursor = null;
        try {
            cursor = r.db("rethinkdb/chat").table("messages").changes()
                                         .getField("new_val")
                                         .run(connectionFactory.createConnection(), ChatMessage.class);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        while (cursor.hasNext()){
            ChatMessage chatMessage = cursor.next();
            log.info("New message: {}", chatMessage.message);
            webSocket.convertAndSend("/topic/messages", chatMessage);
        }
    }
}

/**
 * So what happens here? Each time a change happens in the database, we will get an update.
 * This update will contain two fields: old_val and new_val.
 *
 * Since we are only interested in the new things, we will only retrieve the new_val field.
 *
 * Note that the second (optional) argument to the run method is a class.
 * If present, RethinkDB will try to convert the data to this target class, just like we did in the ChatController above.
 *
 * Then, we simply broadcast the message to all the clients listening on /topic/messages.
 *
 */

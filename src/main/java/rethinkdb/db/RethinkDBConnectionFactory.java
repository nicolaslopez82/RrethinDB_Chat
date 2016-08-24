package rethinkdb.db;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;

import java.util.concurrent.TimeoutException;

/**
 * Created by Nicolas on 8/21/2016.
 *
 * Getting a connection:
 * Every action we will perform on the database will require a Connection.
 * We can create a small factory that we will later use in the code:
 *
 */
public class RethinkDBConnectionFactory {

    private String host;

    public RethinkDBConnectionFactory(String host){
        this.host = host;
    }

    public Connection createConnection() throws TimeoutException {
        return RethinkDB.r.connection().hostname(host).connect();
    }
}

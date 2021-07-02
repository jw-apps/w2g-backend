package backend;

import backend.model.TimestampMessageEncoder;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@ServerEndpoint(value="/websocket", encoders = TimestampMessageEncoder.class)
@Timed
@Metered
@ExceptionMetered
public class Websocket {

    private static final Logger LOGGER = LoggerFactory.getLogger(Websocket.class);

    private static final List<Session> connections = new LinkedList<>();

    @OnOpen
    public void start(Session session) {
        connections.add(session);
        session.setMaxIdleTimeout(0);
        LOGGER.info("New Connection");
    }

    @OnMessage
    public void recvMesssage(final Session session, String message) {
        LOGGER.info("Received message: " + message);
        broadcast(message);
    }

    private void remove(Session session) {
        connections.remove(session);
    }

    @OnClose
    public void end(final Session session) {
        remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOGGER.warn("", throwable);
        remove(session);
    }

    public static void broadcast(String data) {
        for (Session session : connections) {
            try {
                session.getBasicRemote().sendText(data);
            } catch (IOException e) {
                LOGGER.warn("", e);
                connections.remove(session);
                try {
                    session.close();
                } catch (IOException e1) {
                    LOGGER.error("", e);
                }
            }
        }
    }
}

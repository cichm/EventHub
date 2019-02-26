package net.usermd.mcichon.bus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bus {
    private ExecutorService pool;
    private Map<String, List<Action>> actions;

    private Bus(ExecutorService pool, Map<String, List<Action>> actions) {
        this.pool = pool;
        this.actions = actions;
    }

    public static Bus initBus(int threads) throws IllegalArgumentException {
        if (threads < 0 || threads > 100) {
            throw new IllegalArgumentException();
        }

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        Map<String, List<Action>> actions = new ConcurrentHashMap<>();
        return new Bus(pool, actions);
    }

    public MessageBuilder message(String subject) {
        return new MessageBuilder(pool, actions, subject);
    }

    public ResponseBuilder responseFor(String subject) {
        return new ResponseBuilder(actions, subject);
    }
}

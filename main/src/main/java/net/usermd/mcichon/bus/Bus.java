package net.usermd.mcichon.bus;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bus {
    private static class SynchronousBus implements MessageBus {
        private ExecutorService pool;
        private Map<String, List<Action>> actions;

        SynchronousBus() {}

        public SynchronousBus initBus(int threads) throws IllegalArgumentException {
            if (threads < 0 || threads > 100) {
                throw new IllegalArgumentException();
            }

            ExecutorService pool = Executors.newFixedThreadPool(threads);
            Map<String, List<Action>> actions = new ConcurrentHashMap<>();

            this.pool = pool;
            this.actions = actions;

            return this;
        }

        public MessageBuilder message(String subject) {
            return new MessageBuilder(pool, actions, subject);
        }

        public ResponseBuilder emit(String subject) {
            return new ResponseBuilder(actions, subject);
        }
    }

    public static synchronized MessageBus createSingletonSynchronousEventBus() {
        return EventBus.SYNCHRONOUS.messageBus;
    }

    public static synchronized MessageBus createSingletonAsynchronousEventBus() {
        return EventBus.SYNCHRONOUS.messageBus;
    }

    private enum EventBus {
        SYNCHRONOUS(new SynchronousBus()), ASYNCHRONOUS(new SynchronousBus());

        private MessageBus messageBus;

        EventBus(MessageBus messageBus) {
            this.messageBus = messageBus;
        }
    }
}

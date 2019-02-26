package net.usermd.mcichon.bus;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class MessageBuilder {
    private ExecutorService pool;
    private Map<String, List<Action>> actions;
    private String subject;

    MessageBuilder(ExecutorService pool, Map<String, List<Action>> actions, String subject) {
        this.pool = pool;
        this.actions = actions;
        this.subject = subject;
    }

    public void send() {
        actions.computeIfAbsent(subject, __ -> new LinkedList<>())
                .forEach(action -> pool.submit(action::execute));
    }
}

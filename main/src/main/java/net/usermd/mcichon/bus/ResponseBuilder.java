package net.usermd.mcichon.bus;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResponseBuilder {
    private Map<String, List<Action>> actions;
    private String subject;

    ResponseBuilder(Map<String, List<Action>> actions, String subject) {
        this.actions = actions;
        this.subject = subject;
    }

    public ResponseBuilder then(Action action) {
        actions.computeIfAbsent(subject, __ -> new LinkedList<>()).add(action);
        return this;
    }
}

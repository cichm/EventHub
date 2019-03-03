package net.usermd.mcichon.Different;

import net.usermd.mcichon.Dispatcher.DispatcherImpl;

import java.util.concurrent.Executor;
import java.util.logging.Logger;

public class DifferentBus {
    private static final Logger logger = Logger.getLogger(DifferentBus.class.getName());
    private final String identifier;
    private final Executor executor;
    private final DispatcherImpl dispatcher;

    public DifferentBus(String identifier, Executor executor, DispatcherImpl dispatcher) {
        this.identifier = identifier;
        this.executor = executor;
        this.dispatcher = dispatcher;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Executor getExecutor() {
        return executor;
    }
}

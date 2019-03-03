package net.usermd.mcichon.Executor;

import java.util.concurrent.Executor;

abstract public class Executors {
    static final Executor getExecutor() {
        return Runnable::run;
    }
}

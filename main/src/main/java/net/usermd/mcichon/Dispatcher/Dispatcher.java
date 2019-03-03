package net.usermd.mcichon.Dispatcher;

public interface Dispatcher<T> {
    T dispatch(Object event);
}

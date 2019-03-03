package net.usermd.mcichon.BusTypes;

public interface Bus<T> {
    T process(Object event);
}

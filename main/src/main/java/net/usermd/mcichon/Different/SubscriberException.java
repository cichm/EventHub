package net.usermd.mcichon.Different;

import java.lang.reflect.Method;

class SubscriberException {
    private final DifferentBus bus;
    private final Object eventObject;
    private final Object subscriber;
    private final Method method;

    SubscriberException(DifferentBus bus, Object eventObject, Object subscriber, Method method) {
        this.bus = bus;
        this.eventObject = eventObject;
        this.subscriber = subscriber;
        this.method = method;
    }

    public DifferentBus getBus() {
        return this.bus;
    }

    public Object getEventObject() {
        return this.eventObject;
    }

    public Object getSubscriber() {
        return this.subscriber;
    }

    public Method getMethod() {
        return this.method;
    }
}

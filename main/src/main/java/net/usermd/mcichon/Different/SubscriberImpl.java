package net.usermd.mcichon.Different;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

class SubscriberImpl {
    private final DifferentBus bus;
    private final Object target;
    private final Method method;

    SubscriberImpl(DifferentBus bus, Object target, Method method) {
        this.bus = bus;
        this.target = target;
        this.method = method;
    }

    static SubscriberImpl initialize(DifferentBus bus, Object target, Method method) {
        return method.getAnnotation(AcceptConcurrentEvents.class) != null ? new SafeSubscriber(bus, target, method) : new SubscriberImpl(bus, target, method);
    }

    void invokeMethod(Object o) throws InvocationTargetException, IllegalAccessException {
        if (o == null) {
            throw new NullPointerException();
        }
        else {
            this.method.invoke(this.target, o);
        }
    }

    private static final class SafeSubscriber extends SubscriberImpl {
        SafeSubscriber(DifferentBus bus, Object target, Method method) {
            super(bus, target, method);
        }

        @Override
        void invokeMethod(Object event) throws InvocationTargetException, IllegalAccessException {
            synchronized (this) {
                super.invokeMethod(event);
            }
        }
    }

    private SubscriberException context(Object event) {
        return new SubscriberException(bus, event, target, method);
    }

    // TODO:
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriberImpl)) return false;
        SubscriberImpl that = (SubscriberImpl) o;
        return Objects.equals(bus, that.bus) &&
                Objects.equals(target, that.target) &&
                Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bus, target, method);
    }
}

package net.usermd.mcichon.Different;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

class SubscriberRegistry {
    private final DifferentBus bus;

    private final Map<Class<?>, CopyOnWriteArraySet<SubscriberImpl>> subscribers = new ConcurrentHashMap<>();

    SubscriberRegistry(DifferentBus bus) {
        this.bus = bus;
    }

    void register(Object listener) {
        Map<Class<?>, Collection<SubscriberImpl>> listenerMethods = findAllSubscribers(listener);

        for (Map.Entry<Class<?>, Collection<SubscriberImpl>> entry : listenerMethods.entrySet()) {
            Class<?> type = entry.getKey();
            Collection<SubscriberImpl> methodsInListener = entry.getValue();

            Collection<SubscriberImpl> eventSubscribers = subscribers.computeIfAbsent(type,
                    aClass -> new CopyOnWriteArraySet<>());
            eventSubscribers.addAll(methodsInListener);
        }
    }

    void unregister(Object listener) {
        Map<Class<?>, Collection<SubscriberImpl>> listenerMethods = findAllSubscribers(listener);

        for (Map.Entry<Class<?>, Collection<SubscriberImpl>> entry : listenerMethods.entrySet()) {
            Class<?> type = entry.getKey();
            Collection<SubscriberImpl> listenerMethodsForType = entry.getValue();

            CopyOnWriteArraySet<SubscriberImpl> subscribers = this.subscribers.get(type);

            if (subscribers != null)
                subscribers.removeAll(listenerMethodsForType);

        }
    }

    private Map<Class<?>, Collection<SubscriberImpl>> findAllSubscribers(Object listener) {
        Map<Class<?>, Collection<SubscriberImpl>> subscriberMap = new HashMap<>();
        Class<?> currentClass = listener.getClass();

        while (currentClass != null) {
            for (Method method : currentClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Subscribe.class) && !method.isSynthetic()) {
                    if (method.getParameterCount() != 1) {
                        throw new IllegalArgumentException(MessageFormat.format("Method {0} has @Subscribe annotation " +
                                        "but has {1} parameters. Subscriber methods must have exactly 1 parameter",
                                method, method.getParameterCount()));
                    }

                    Class<?> type = method.getParameterTypes()[0];
                    Collection<SubscriberImpl> subscribers = subscriberMap.computeIfAbsent(type, aClass -> new ArrayList<>());
                    subscribers.add(SubscriberImpl.initialize(bus, listener, method));
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return subscriberMap;
    }

    Iterator<SubscriberImpl> getAllSubscribers(Object event) {
        CopyOnWriteArraySet<SubscriberImpl> eventSubscribers = this.subscribers.get(event.getClass());
        if (eventSubscribers != null) {
            ArrayList<SubscriberImpl> subscribers = new ArrayList<>(eventSubscribers.size());
            subscribers.addAll(eventSubscribers);
            return subscribers.iterator();
        } else {
            return Collections.emptyIterator();
        }
    }
}

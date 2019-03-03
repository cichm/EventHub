package net.usermd.mcichon.Different;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

class SubscriberRegistry {
    /** The event bus this registry belongs to. */
    private final DifferentBus bus;

    private final Map<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers = new ConcurrentHashMap<>();

    SubscriberRegistry(DifferentBus bus) {
        this.bus = bus;
    }

    /**
     * Register all subscriber methods of the {@code listener} object
     *
     * @param listener Object with subscriber methods
     */
    void register(Object listener) {
        Map<Class<?>, Collection<Subscriber>> listenerMethods = findAllSubscribers(listener);

        for (Map.Entry<Class<?>, Collection<Subscriber>> entry : listenerMethods.entrySet()) {
            Class<?> type = entry.getKey();
            Collection<Subscriber> methodsInListener = entry.getValue();

            Collection<Subscriber> eventSubscribers = subscribers.computeIfAbsent(type,
                    aClass -> new CopyOnWriteArraySet<>());
            eventSubscribers.addAll(methodsInListener);
        }
    }

    /**
     * Unregister all subscriber methods of the {@code listener} object
     * @param listener Object with subscriber methods
     */
    void unregister(Object listener) {
        Map<Class<?>, Collection<Subscriber>> listenerMethods = findAllSubscribers(listener);

        for (Map.Entry<Class<?>, Collection<Subscriber>> entry : listenerMethods.entrySet()) {
            Class<?> type = entry.getKey();
            Collection<Subscriber> listenerMethodsForType = entry.getValue();

            CopyOnWriteArraySet<Subscriber> subscribers = this.subscribers.get(type);

            if (subscribers != null)
                subscribers.removeAll(listenerMethodsForType);

        }
    }

    /**
     * Find all subscriber methods of the particular listener (Including superclasses)
     *
     * @param listener Object with subscriber methods
     * @return all the subscriber methods wrapped in {@link Subscriber} and mapped to event type
     */
    private Map<Class<?>, Collection<Subscriber>> findAllSubscribers(Object listener) {
        Map<Class<?>, Collection<Subscriber>> subscriberMap = new HashMap<>();
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
                    Collection<Subscriber> subscribers = subscriberMap.computeIfAbsent(type, aClass -> new ArrayList<>());
                    subscribers.add(Subscriber.create(bus, listener, method));
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return subscriberMap;
    }

    /**
     * @param event Event which are going to be dispatched
     * @return all subscriber methods for the {@code event}
     */
    Iterator<Subscriber> getAllSubscribers(Object event) {
        CopyOnWriteArraySet<Subscriber> eventSubscribers = this.subscribers.get(event.getClass());
        if (eventSubscribers != null) {
            ArrayList<Subscriber> subscribers = new ArrayList<>(eventSubscribers.size());
            subscribers.addAll(eventSubscribers);
            return subscribers.iterator();
        } else {
            return Collections.emptyIterator();
        }
    }
}

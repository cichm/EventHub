package bus;

import net.usermd.mcichon.bus.Action;
import net.usermd.mcichon.bus.Bus;
import net.usermd.mcichon.bus.MessageBus;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        MessageBus bus = Bus.createSingletonSynchronousEventBus().initBus(2);

        AtomicInteger counter = new AtomicInteger(0);
        String subject = "testMessage";

        Action action = () -> System.out.println("Hello World!");

        bus.emit(subject).then(action);
        bus.message(subject).send();
    }
}

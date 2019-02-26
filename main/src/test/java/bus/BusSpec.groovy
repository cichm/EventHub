package bus

import com.jayway.awaitility.Awaitility
import net.usermd.mcichon.bus.Bus
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

class BusSpec extends Specification {
    def "maximum of two numbers"(int a, int b, int c) {
        expect:
        Math.max(a, b) == c

        where:
        a | b | c
        1 | 3 | 3
        7 | 4 | 7
        0 | 0 | 0
    }

    def "check simple send"() {
        def bus = Bus.initBus(1)

        def counter = new AtomicInteger(0)
        def subject = "testMessage"

        when:
        bus.responseFor(subject).then({counter.incrementAndGet()})
        bus.message(subject).send();

        then:
        Awaitility.await().until({counter.get() == 1})
    }
}

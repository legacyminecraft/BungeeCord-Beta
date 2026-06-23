package net.md_5.bungee.event;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class EventPriorityTest {

    private final EventBus bus = new EventBus();
    private final CountDownLatch latch = new CountDownLatch(5);

    @Test
    public void testPriority() {
        bus.register(this);
        bus.register(new EventPriorityListenerPartner());
        bus.post(new PriorityTestEvent());
        assertEquals(0, latch.getCount());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLowestPriority(PriorityTestEvent event) {
        assertEquals(5, latch.getCount());
        latch.countDown();
    }

    @EventHandler
    public void onNormalPriority(PriorityTestEvent event) {
        assertEquals(3, latch.getCount());
        latch.countDown();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onHighestPriority(PriorityTestEvent event) {
        assertEquals(1, latch.getCount());
        latch.countDown();
    }

    public static class PriorityTestEvent {
    }

    public class EventPriorityListenerPartner {

        @EventHandler(priority = EventPriority.HIGH)
        public void onHighPriority(PriorityTestEvent event) {
            assertEquals(2, latch.getCount());
            latch.countDown();
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onLowPriority(PriorityTestEvent event) {
            assertEquals(4, latch.getCount());
            latch.countDown();
        }
    }
}

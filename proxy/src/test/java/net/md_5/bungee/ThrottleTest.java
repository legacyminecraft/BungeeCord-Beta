package net.md_5.bungee;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

public class ThrottleTest {

    @Test
    public void testThrottle() throws InterruptedException, UnknownHostException {
        ConnectionThrottle throttle = new ConnectionThrottle(5);
        InetAddress address;

        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            address = InetAddress.getByName(null);
        }

        assertFalse(throttle.throttle(address), "Address should not be throttled");
        assertTrue(throttle.throttle(address), "Address should be throttled");

        throttle.unthrottle(address);
        assertFalse(throttle.throttle(address), "Address should not be throttled");

        Thread.sleep(15);
        assertFalse(throttle.throttle(address), "Address should not be throttled");
    }
}

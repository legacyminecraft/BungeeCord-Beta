package net.md_5.bungee.protocol;

import net.md_5.bungee.protocol.packet.AbstractPacketHandler;
import net.md_5.bungee.protocol.packet.DefinedPacket;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class PacketTest {

    @Test
    public void testPackets() throws Exception {
        AbstractPacketHandler handler = new AbstractPacketHandler() {
        };

        for (short i = 0; i < 256; i++) {
            Class<? extends DefinedPacket> clazz = Vanilla.getInstance().getClasses()[i];
            if (clazz != null) {
                assertTrue(Modifier.isPublic(clazz.getModifiers()), "Packet " + clazz + " is not public");
                DefinedPacket packet = Vanilla.packet(i, Vanilla.getInstance());
                assertTrue(packet != null, "Could not create packet with id " + i + " and class " + clazz);
                assertTrue(packet.getClass() == clazz, "Packet with id " + i + " does not have correct class (expected " + clazz + " but got " + packet.getClass());
                assertTrue(packet.getId() == i, "Packet " + clazz + " does not report correct id");
                assertTrue(packet.hashCode() != System.identityHashCode(packet), "Packet " + clazz + " does not have custom hash code");
                assertTrue(packet.toString().indexOf('@') == -1, "Packet " + clazz + " does not have custom toString");
                // TODO: Enable this test again in v2
                // Assert.assertTrue( "Packet " + clazz + " does not have private no args constructor", Modifier.isPrivate( clazz.getDeclaredConstructor().getModifiers() ) );

                for (Field field : clazz.getDeclaredFields()) {
                    // TODO: Enable this test again in v2
                    // Assert.assertTrue( "Packet " + clazz + " has non private field " + field, Modifier.isPrivate( field.getModifiers() ) );
                }

                packet.handle(handler); // Make sure there are no exceptions
            }
        }
    }
}

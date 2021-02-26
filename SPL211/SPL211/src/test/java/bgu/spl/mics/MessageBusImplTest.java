package bgu.spl.mics;

import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    private MessageBusImpl messageBus;
    private MicroService m1_register;
    private MicroService m2_register;
    private MicroService m_notregister;
    private Event<String> event1;
    private Event<String> event2;
    private Broadcast brodcast;

    @BeforeEach
    void setUp() {
        messageBus = messageBus.getInstance();
        m1_register = new HanSoloMicroservice();
        m2_register = new HanSoloMicroservice();
        m_notregister = new HanSoloMicroservice();
        messageBus.register(m1_register);
        messageBus.register(m2_register);
        brodcast = new ExampleBroadcast("broadcast1");
        event1 = new ExampleEvent("event1");
        event2 = new ExampleEvent("event2");
    }

    @Test
    void subscribeEvent() {
        try{
            messageBus.subscribeEvent(ExampleEvent.class, m_notregister);
            fail("Exception expected!");
        } catch (IllegalStateException e){
            //success
        }

        messageBus.subscribeEvent(ExampleEvent.class, m1_register);
        messageBus.sendEvent(event1);
        try {
            assertEquals(event1, messageBus.awaitMessage(m1_register));
        } catch (InterruptedException e) {
            fail("No Exception expected!");
        }
    }

    @Test
    void subscribeBroadcast() {
        try{
            messageBus.subscribeBroadcast(ExampleBroadcast.class, m_notregister);
            fail("Exception expected!");
        } catch (IllegalStateException e){
            //success
        }

        messageBus.subscribeBroadcast(ExampleBroadcast.class, m1_register);
        messageBus.sendBroadcast(brodcast);
        try {
            assertEquals(brodcast, messageBus.awaitMessage(m1_register));
        } catch (InterruptedException e) {
            fail("No Exception expected!");
        }
    }

    @Test
    void complete() {
        messageBus.subscribeEvent(ExampleEvent.class, m1_register);
        Future<String> future = messageBus.sendEvent(event1);
        messageBus.complete(event1, "Yes");
        assertTrue(future.isDone());
        assertEquals("Yes", future.get());
    }

    @Test
    void sendBroadcast() {
        messageBus.subscribeBroadcast(ExampleBroadcast.class, m1_register);
        messageBus.subscribeBroadcast(ExampleBroadcast.class, m2_register);
        messageBus.sendBroadcast(brodcast);
        try {
            assertEquals(brodcast, messageBus.awaitMessage(m1_register));
            assertEquals(brodcast, messageBus.awaitMessage(m2_register));
        } catch (InterruptedException e) {
            fail("No Exception expected!");
        }
    }

    @Test
    void sendEvent() {
        messageBus.subscribeEvent(ExampleEvent.class, m1_register);
        messageBus.subscribeEvent(ExampleEvent.class, m2_register);
        messageBus.sendEvent(event1);
        messageBus.sendEvent(event2);
        try {
            assertEquals(event1, messageBus.awaitMessage(m1_register));
            assertEquals(event2, messageBus.awaitMessage(m2_register));
        } catch (InterruptedException e) {
            fail("No Exception expected!");
        }
    }

    @Test
    void register() {
        try {
            messageBus.register(m1_register);
            fail("Exception expected!");
        } catch (IllegalStateException e) {
            //success
        }
        messageBus.register(m_notregister);
        messageBus.subscribeEvent(ExampleEvent.class, m_notregister);
    }


    @Test
    void awaitMessage() {
        try {
            try {
                messageBus.awaitMessage(m_notregister);
            } catch (InterruptedException e) {
                fail("No Interrupted Exception expected!");
            }
            fail("Exception expected!");
        } catch (IllegalStateException e) {
            //success
        }
        messageBus.subscribeBroadcast(ExampleBroadcast.class, m1_register);
        messageBus.sendBroadcast(brodcast);
        try {
            assertEquals(brodcast, messageBus.awaitMessage(m1_register));
        } catch (InterruptedException e) {
            fail("No Exception expected!");
        }
    }
}
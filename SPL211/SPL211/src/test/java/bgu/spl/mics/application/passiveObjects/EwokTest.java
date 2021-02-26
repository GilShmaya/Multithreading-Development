package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {

    private Ewok ewok;

    @BeforeEach
    public void setUp(){
        ewok = new Ewok(0);
    }


    @Test
    void acquire() {
        assertTrue(ewok.available);
        ewok.acquire();
        assertFalse(ewok.available);
        try {
            ewok.acquire();
            fail("Exception expected!");
        } catch (IllegalStateException e) {
            //success
        }
    }

    @Test
    void release() {
        assertTrue(ewok.available);
        try {
            ewok.release();
            fail("Exception expected!");
        } catch (IllegalStateException e) {
            //success
        }
        assertTrue(ewok.available);
        ewok.acquire();
        assertFalse(ewok.available);
        ewok.release();
        assertTrue(ewok.available);
    }
}
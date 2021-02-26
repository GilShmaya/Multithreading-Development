package bgu.spl.mics.application.passiveObjects;


import java.util.List;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private static Ewoks Ewoks_instance = null;

    private Ewok[] EwoksArray;

    private Ewoks() {
        initialize(0);
    }

    public static Ewoks getInstance()
    {
        if (Ewoks_instance == null)
            Ewoks_instance = new Ewoks();

        return Ewoks_instance;
    }

    public void initialize(int numOfEwoks){
        EwoksArray = new Ewok[numOfEwoks];

        for(int i=0 ; i < numOfEwoks ; i++)
            EwoksArray[i] = new Ewok(i+1);
    }

    public void acquire(List<Integer> serials) throws InterruptedException {
        serials.sort(Integer::compareTo);

        for(Integer i: serials) {
            synchronized(EwoksArray[i - 1]) {
                while (!EwoksArray[i - 1].getAvailable()) {
                    EwoksArray[i - 1].wait();
                }
                EwoksArray[i - 1].acquire();
            }
        }
    }

    public void release(List<Integer> serials) throws InterruptedException {
        for(Integer i: serials) {
            synchronized (EwoksArray[i - 1]) {
                EwoksArray[i - 1].release();
                EwoksArray[i - 1].notifyAll();
            }
        }
    }




}

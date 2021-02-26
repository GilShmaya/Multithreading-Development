package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private long duration;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
    }

    @Override
    protected void initialize() {
        Diary diary = Diary.getInstance();

        // when microservice gets this broadcast it activates the callback that terminate.
        subscribeBroadcast(TerminateBroadcast.class, terminatecallback->{
            terminate();
            diary.setLandoTerminate(System.currentTimeMillis());
        });

        subscribeEvent(BombDestroyerEvent.class, bombDestroyerevent->{
            try {
                Thread.sleep(duration);
            }catch ( InterruptedException e){}

            MessageBusImpl.getInstance().complete(bombDestroyerevent ,true);
        });

        Main.startLeia.countDown();


    }

}

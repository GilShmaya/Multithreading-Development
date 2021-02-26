package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
	
    public C3POMicroservice() {
        super("C3PO");
    }

    @Override
    protected void initialize() {
        Diary diary = Diary.getInstance();
        // when a microservice gets this broadcast it activates the callback that terminate.
        subscribeBroadcast(TerminateBroadcast.class, terminatecallback->{
            terminate();
            diary.setC3POTerminate(System.currentTimeMillis());
        });

        // when a microservice gets this event it need to attack
        subscribeEvent(AttackEvent.class, attackEvent->{
            Ewoks ewoks = Ewoks.getInstance();

            try {
                ewoks.acquire(attackEvent.getAttack().getSerials());
                Thread.sleep(attackEvent.getAttack().getDuration());
                ewoks.release(attackEvent.getAttack().getSerials());
            }catch ( InterruptedException e){}

            MessageBusImpl.getInstance().complete(attackEvent ,true);
            diary.setTotalAttacks(diary.getTotalAttacks()+1);
        });

        subscribeBroadcast(FinishedAttackBroadcast.class, finishedAttackcallback->{
            diary.setC3POFinish(System.currentTimeMillis());
        });

        Main.startLeia.countDown();

    }

}

package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	private Queue<Future> futureQueue;
	
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
        futureQueue = new LinkedBlockingQueue<Future>();
    }

    @Override
    protected void initialize() {
        Diary diary = Diary.getInstance();

        // when microservice gets this broadcast it activates the callback that terminate.
        subscribeBroadcast(TerminateBroadcast.class, terminateEvent->{
            terminate();
            diary.setLeiaTerminate(System.currentTimeMillis());
        });
        for(Attack attack: attacks){
            Future<Boolean> future = sendEvent(new AttackEvent(attack));
            if( future == null)
                throw new IllegalStateException("No Micro-Service has registered to handle AttackEvent events! The event cannot be processed");
            futureQueue.add(future);
        }

        // send a broadcast to Hansolo & C3PO which they will get after finishing all there attacks
        sendBroadcast(new FinishedAttackBroadcast());

        //wait until all the attack events are done
        while(!futureQueue.isEmpty())
            futureQueue.remove().get();

        //after all the attack events are done, send a DeactivationEvent to R2D2
        Future<Boolean> Deactivatefuture = sendEvent(new DeactivationEvent());

        Deactivatefuture.get();

        // send BombDestroyerEvent to Lando when all is set for his final action
        Future<Boolean> BombDestroyerfuture = sendEvent(new BombDestroyerEvent());

        // after all finished, Leia send TerminateBroadcast
        sendBroadcast(new TerminateBroadcast());

    }
}

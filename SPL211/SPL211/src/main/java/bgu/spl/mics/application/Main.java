package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static CountDownLatch startLeia;

	public static void main(String[] args) throws InterruptedException {
		startLeia = new CountDownLatch(4);

		//if(args == null){
		//	throw new NullPointerException("add input to args");
		//}
		Diary diary = Diary.getInstance();

		Input input = new Input();
		try{
			input = JsonInputReader.getInputFromJson(args[0]);
		}catch (IOException e){}

		Ewoks ewoks = Ewoks.getInstance();
		ewoks.initialize(input.getEwoks());

		Thread HanSolo = new Thread(new HanSoloMicroservice());
		Thread C3PO = new Thread(new C3POMicroservice());
		Thread R2D2 = new Thread(new R2D2Microservice(input.getR2D2()));
		Thread Lando = new Thread(new LandoMicroservice(input.getLando()));
		Thread Leia = new Thread(new LeiaMicroservice(input.getAttacks()));

		HanSolo.start();
		C3PO.start();
		Lando.start();
		R2D2.start();

		startLeia.await();

		Leia.start();

		HanSolo.join();
		C3PO.join();
		Lando.join();
		R2D2.join();
		Leia.join();

		try {
			JsonInputWriter.getStringToJson(diary, args);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

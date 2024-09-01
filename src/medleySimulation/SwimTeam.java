//M. M. Kuttel 2024 mkuttel@gmail.com
//Class to represent a swim team - which has four swimmers
package medleySimulation;

import medleySimulation.Swimmer.SwimStroke;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class SwimTeam extends Thread {
	
	public static StadiumGrid stadium; //shared 
	private Swimmer [] swimmers;
	private int teamNo; //team number
	private CountDownLatch[] latches;

	public static final int sizeOfTeam=4;
	
	SwimTeam( int ID, FinishCounter finish,PeopleLocation [] locArr ) {
		this.teamNo=ID;

		swimmers= new Swimmer[sizeOfTeam];
	    SwimStroke[] strokes = SwimStroke.values();  // Get all enum constants
		stadium.returnStartingBlock(ID);

		latches = new CountDownLatch[sizeOfTeam - 1]; // Latches for sequential execution

		for (int i = 0; i < sizeOfTeam - 1; i++) {
			latches[i] = new CountDownLatch(1); // Initialize latches
		}

		for(int i=teamNo*sizeOfTeam,s=0;i<((teamNo+1)*sizeOfTeam); i++,s++) { //initialise swimmers in team
			locArr[i]= new PeopleLocation(i,strokes[s].getColour());
	      	int speed=(int)(Math.random() * (3)+30); //range of speeds
			swimmers[s] = new Swimmer(i,teamNo,locArr[i],finish,speed,strokes[s]
			,(s == 0) ? null : latches[s - 1], (s == sizeOfTeam - 1) ? null : latches[s]); //hardcoded speed for now
		}
	}
	
	
	public void run() {
		try {
			for(int s=0;s<sizeOfTeam; s++) { //start swimmer threads
				swimmers[s].start();
			}

			for(int s=0;s<sizeOfTeam; s++) swimmers[s].join();			//don't really need to do this;

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
	


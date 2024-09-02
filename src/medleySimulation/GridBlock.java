//M. M. Kuttel 2024 mkuttel@gmail.com
// GridBlock class to represent a block in the grid.
// only one thread at a time "owns" a GridBlock - this must be enforced

package medleySimulation;


import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GridBlock {
	
	private final AtomicInteger isOccupied = new AtomicInteger();
	
	private final AtomicBoolean isStart = new AtomicBoolean();  //is this a starting block?
	private int [] coords; // the coordinate of the block.
	
	GridBlock(boolean startBlock) throws InterruptedException {
		isStart.set(startBlock);
		isOccupied.set(-1);
	}
	
	GridBlock(int x, int y, boolean startBlock) throws InterruptedException {
		this(startBlock);
		coords = new int [] {x,y};
	}
	
	public synchronized int getX() {return coords[0];}
	
	public synchronized int getY() {return coords[1];}
	
	//Get a block
	public synchronized boolean get(int threadID) throws InterruptedException {
		if (isOccupied.get()==threadID) return true; //thread Already in this block
		if (isOccupied.get()>=0) return false; //space is occupied
		isOccupied.set(threadID);  //set ID to thread that had block
		return false;
	}

	//release a block
	public synchronized void release() {
		isOccupied.set(-1);
	}

	//is a bloc already occupied?
	public  boolean occupied() {
		if(isOccupied.get()==-1) return false;
		return true;
	}

	//is a start block
	public  boolean isStart() {
		return isStart.get();
	}

}

package reuze;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.PriorityBlockingQueue;


public class as_SimulationDiscrete {
	static int threadCount, delayCount;
	static PriorityBlockingQueue<as_SimulationThread> q=new PriorityBlockingQueue<as_SimulationThread>();
	public static void Hold (double delay) {
		if (delay < 0) throw new RuntimeException("wait time less than zero");
		if (!(Thread.currentThread() instanceof as_SimulationThread)) throw new RuntimeException("not a simulation thread");
		as_SimulationThread t=(as_SimulationThread)Thread.currentThread();
		t.eventTime=as_SimulationThread.clock+delay;
		System.out.println("time="+as_SimulationThread.clock+" delaying "+t);		
		delayCount++;
		if (delayCount<threadCount) {t.block(q); return;}
		as_SimulationThread t1=q.peek();
		if (t1==null) {as_SimulationThread.clock+=delay; delayCount--; System.out.println("time="+as_SimulationThread.clock+" running "+t); return;}
		int i=t.compareTo(t1);
		if (i<0) {as_SimulationThread.clock+=delay; delayCount--; System.out.println("time="+as_SimulationThread.clock+" running "+t); return;}
		check();
		t.block(q);
	}
	private static void check() {
		if (delayCount<threadCount) return;
		as_SimulationThread t=null;
		//System.out.println(q);
		try {
			t=q.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		as_SimulationThread.clock=t.eventTime;
		System.out.println("time="+as_SimulationThread.clock+" running "+t);
		delayCount--;
		try {
			t.bar.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	public static void Hold (int delay) { Hold((double)delay); }
	public static void Hold (float delay) { Hold((double)delay); }
	public static void Hold (long delay) { Hold((double)delay); }
	public static double time() { return as_SimulationThread.clock; }
	public static void PrintStats() { }
	public static void main(String args[]) {
		new as_SimulationStart().start();
	}
	public static void newThread() {
		threadCount++;
	}
	public static void stopThread() {
		threadCount--;
		if (threadCount<=0) {
			System.out.println("TIME="+as_SimulationThread.clock);
			as_SimulationEntity.Print();
			da_Histogram.Print();
			System.exit(0);
		}
		check();
	}
}

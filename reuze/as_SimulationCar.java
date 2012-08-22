package reuze;

public class as_SimulationCar extends as_SimulationThread {
    static as_SimulationEntity carpark=new as_SimulationEntity("carpark",5);
    static da_Histogram waittime=new da_Histogram("wait time",50,10,20);
	public as_SimulationCar(String name) {
		super(name);
	}
	static int x=100;
	@Override
	public void run() {
		double start=clock;
		carpark.Enter(1,0);
		as_SimulationDiscrete.Hold(--x);
		carpark.Leave(1);
		waittime.Tabulate(clock-start);
		exit();
	}
}

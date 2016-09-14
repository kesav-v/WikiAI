public class SynchroTest extends Thread {

	private int c = 0;
	private int number;

	public SynchroTest(int number) {
		this.number = number;
	}

	public synchronized void incr() {
		c++;
	}

	public synchronized void decr() {
		c--;
	}

	public synchronized void print() {
		System.out.println("Thread " + number + " - Count = " + c);
	}

	public void run() {
		incr();
		print();
		decr();
		print();
		decr();
		print();
	}

	public static void main(String[] args) {
		SynchroTest[] sts = new SynchroTest[2];
		for (int i = 0; i < sts.length; i++) {
			sts[i] = new SynchroTest(i + 1);
			sts[i].start();
		}	
	}
}
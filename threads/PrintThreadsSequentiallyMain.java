package learn.threads;

public class PrintThreadsSequentiallyMain {

	public static void main(String[] args) {
		PrintSequenceRunnable runnable1=new PrintSequenceRunnable(1);
        PrintSequenceRunnable runnable2=new PrintSequenceRunnable(2);
        PrintSequenceRunnable runnable3=new PrintSequenceRunnable(0);
 
        Thread t1=new Thread(runnable1,"T1");
        Thread t2=new Thread(runnable2,"T2");
        Thread t3=new Thread(runnable3,"T3");
 
        t1.start();
        t2.start();
        t3.start();   
	}

}

class PrintSequenceRunnable  implements Runnable{
	private static int count = 1; //very important point => this has to be static as this counter value will be shared.
	private int n = 10;
	private int remainder;
	static Object lock=new Object(); //lock obj is shared as it is static.
	
	public PrintSequenceRunnable (int remainder) {
		this.remainder = remainder;
	}

	@Override
	public void run() {
			while (count < n-1) {
				synchronized (lock) {
				while (count % 3 != remainder) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println(Thread.currentThread().getName()+":"+count);
				count++;
				lock.notifyAll();
			}
		}		
	}
}
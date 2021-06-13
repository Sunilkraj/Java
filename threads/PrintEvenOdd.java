package learn.threads;

public class PrintEvenOdd {

	public static void main(String[] args) {
		/*PrintNumbers printNumbers = new PrintNumbers();
		Thread evenThread = new Thread() {
			public void run() {
				printNumbers.printEven();
			}
		};

		Thread oddThread = new Thread() {
			public void run() {
				printNumbers.printOdd();
			}
		};

		evenThread.start();
		oddThread.start();*/

		PrintEvenOddInSeq printOdd = new PrintEvenOddInSeq(1);
		PrintEvenOddInSeq printEven = new PrintEvenOddInSeq(0);
		
		Thread t1 = new Thread(printOdd, "odd");
		Thread t2 = new Thread(printEven, "even");
		
		t1.start();
		t2.start();
	}

}

class PrintNumbers {
	private int count = 1;
	private boolean isOdd = true;
	private int n = 10;

	
	public void printEven() {
		 try {
	            Thread.sleep(1000);
	        } catch (InterruptedException e1) {
	            e1.printStackTrace();
	        }
		synchronized (this) {
			while (count < n) {
				while (isOdd) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("even: "+count);
				count++;
				isOdd = true;
				notify();

			}
		}
	}

	public void printOdd() {
		synchronized (this) {
			while (count < n) {
				while (!isOdd) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("odd: "+count);
				count++;
				isOdd = false;
				notify();
			}
		}
	}

	
}

class PrintEvenOddInSeq implements Runnable{
	private static int count = 1;
	private int n = 10;
	private int remainder;
	static Object lock=new Object();
	
	public PrintEvenOddInSeq(int remainder) {
		this.remainder = remainder;
	}

	@Override
	public void run() {
			while (count < n) {
				synchronized (lock) {
				while (count % 2 != remainder) {
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
package learn.threads;

import java.util.concurrent.CountDownLatch;

class VideoConference implements Runnable {
	CountDownLatch c = null;
	int n = 0;

	public VideoConference(int n) {
		c = new CountDownLatch(n);
		this.n = n;
	}

	public void arrive(String name) {
		System.out.println(name+" arrived");
		c.countDown();
	}

	@Override
	public void run() {
		try {
			System.out.println("Waiting for "+n+" participants to arrive");
			c.await();
			System.out.println(n+" participants arrived, so starting the conf");
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class Participant implements Runnable {
	VideoConference vc = null;
	String name = null;

	public Participant(VideoConference vc, String name) {
		this.vc = vc;
		this.name = name;
	}

	@Override
	public void run() {
		vc.arrive(name);
	}
}

public class VideoConferenceTest {
	public static void main(String[] args) {
		VideoConference vc = new VideoConference(3);

		Thread vcThread = new Thread(vc);
		vcThread.start();

		for (int i = 0; i < 3; i++) {
			Participant p = new Participant(vc, "Ashwin"+i);
			Thread pThread = new Thread(p);
			pThread.start();
		}
	}

}
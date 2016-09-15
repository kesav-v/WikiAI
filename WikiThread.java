import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class WikiThread extends Thread {

	private ArrayList<Integer> wordCounts = new ArrayList<Integer>();
	private int seconds;
	private WikiReader wr;
	private long start;

	public WikiThread(int seconds, WikiReader reader, long start) {
		this.start = start;
		this.seconds = seconds;
		wr = reader;
	}

	public boolean isRunning() {
		return (getState() == Thread.State.RUNNABLE);
	}

	public int numArticles() {
		return wordCounts.size();
	}

	public void run() {
		while (timeElapsed() <= seconds) {
			wordCounts.add(wr.read());
		}
		System.out.println(this + " is here");
	}

	public double timeElapsed() {
		return (System.nanoTime() - start) / 1E9;
	}

	public ArrayList<Integer> getWordCounts() {
		return wordCounts;
	}
}
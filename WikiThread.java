import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class WikiThread extends Thread {

	private int number;
	private ArrayList<Integer> wordCounts = new ArrayList<Integer>();
	private long time;

	public WikiThread(int num, int seconds) {
		number = num;
		time = seconds;
	}

	public static void main(String[] args) {
		for (int len = 1; len < 1000; len++) {
			WikiThread[] threads = new WikiThread[len];
			for (int i = 0; i < threads.length; i++) {
				threads[i] = new WikiThread(i + 1, 30);
				threads[i].start();
			}
			while (running(threads)) {

			}
			int sum = 0;
			for (WikiThread wa : threads) {
				sum += wa.numArticles();
			}
			System.out.println(len + "\t" + sum);
			Scanner scan = null;
			try {
				scan = new Scanner(new File("articlestats.txt"));
			} catch (Exception e) {
				System.out.println("No file exists at that path yet...");
			}
			String str = "";
			while (len != 1 && scan != null && scan.hasNext()) {
				str += scan.nextLine() + "\n";
			}
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new File("articlestats.txt"));
			} catch (Exception e) {
				System.err.println("ERROR: Invalid path");
				System.exit(1);
			}
			pw.print(str);
			pw.println(len + "\t" + sum);
			scan.close();
			pw.close();
		}
	}

	public static boolean running(WikiThread[] arts) {
		for (WikiThread wa : arts) {
			if (wa.isRunning()) return true;
		}
		return false;
	}

	public boolean isRunning() {
		return (getState() == Thread.State.RUNNABLE);
	}

	public int numArticles() {
		return wordCounts.size();
	}

	public void run() {
		long start = System.nanoTime();
		while ((System.nanoTime() - start) / 1E9 <= time) {
			WikiArticle article = new WikiArticle();
			if (!article.exists()) continue;
			wordCounts.add(article.getContent().split("\\W+").length);
		}
	}

	public String getThreadName() {
		return "Thread-" + number;
	}

	public ArrayList<Integer> getWordCounts() {
		return wordCounts;
	}
}
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class WikiCategories extends Thread {

	private ArrayList<String> wanted = read("wanted.txt");
	private ArrayList<String> unwanted = read("unwanted.txt");
	private ArrayList<String> commons = new ArrayList<String>();
	private ArrayList<WikiArticle> articles = readArticles();
	private int numThreads;
	private static int numArticles;
	private int number;
	private long start;
	double seconds;

	public WikiCategories(long start, int number, int n) {
		this.start = start;
		this.number = number;
		numThreads = n;
		numArticles = 100;
	}

	public static void main(String[] args) {
		int numThreads;
		try {
			numThreads = Integer.parseInt(args[0]);
		} catch (Exception e) {
			Scanner kb = new Scanner(System.in);
			System.out.print("Enter the number of threads -> ");
			numThreads = kb.nextInt();
		}
		WikiCategories[] wcs = new WikiCategories[numThreads];
		long l = System.nanoTime();
		for (int i = 0; i < wcs.length; i++) {
			wcs[i] = new WikiCategories(l, i + 1, numThreads);
			wcs[i].start();
		}
	}

	public void run() {
		try {
			go();
		}
		finally {
			seconds = (System.nanoTime() - start) / 1E9;
			System.out.println("Thread " + number + " finished in " + seconds + " seconds");
		}
	}

	public void go() {
		for (int i = 0; i < numArticles / numThreads; i++) {
			categorize();
		}
	}

	public void categorize() {
		PrintWriter writeWanted = null;
		PrintWriter writeUnwanted = null;
		try {
			writeWanted = new PrintWriter(new File("wanted.txt"));
			writeUnwanted = new PrintWriter(new File("unwanted.txt"));
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: Could not find one or more files");
			System.exit(0);
		}
		for (String s : wanted) {
			writeWanted.println(s);
		}
		for (String s : unwanted) {
			writeUnwanted.println(s);
		}
		WikiArticle wa1 = new WikiArticle();
		articles.add(wa1);
		wa1.findKeyWords(wanted, unwanted, commons, writeUnwanted);
		writeWanted.close();
		writeUnwanted.close();
	}

	public String removeNewLines(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\n') {
				str = str.substring(0, i) + str.substring(i + 1);
			}
		}
		return str;
	}

	public ArrayList<String> read(String filename) {
		Scanner in = null;
		try {
			in = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			return new ArrayList<String>();
		}
		ArrayList<String> words = new ArrayList<String>();
		while (in.hasNext()) {
			words.add(in.nextLine());
		}
		return words;
	}

	public ArrayList<WikiArticle> readArticles() {
		Scanner in = null;
		try {
			in = new Scanner(new File("articles.txt"));
		} catch (FileNotFoundException e) {
			return new ArrayList<WikiArticle>();
		}
		ArrayList<WikiArticle> words = new ArrayList<WikiArticle>();
		while (in.hasNext()) {
			words.add(new WikiArticle(in.nextLine(), in.nextLine(), in.nextLine()));
		}
		return words;
	}
}
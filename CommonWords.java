import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.awt.*;
import javax.swing.*;

public class CommonWords extends JPanel {

	private ArrayList<String> words = new ArrayList<String>();
	private ArrayList<String> constants = new ArrayList<String>();
	private ArrayList<Integer> occurrences = new ArrayList<Integer>();
	private int numArticles = 0;
	private long start = System.currentTimeMillis();

	public static void main(String[] args) {
		CommonWords cw = new CommonWords();
		JFrame frame = new JFrame("Common Words");
		frame.getContentPane().add(cw);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 1000);
		cw.run();
	}

	public void run() {
		System.out.println("Populating list...");
		populateList();
		System.out.println("Finished");
		boolean bool = true;
		for (int i = 0; bool; i++) {
			scanPage();
		}
	}

	public void scanPage() {
		numArticles++;
		HTMLDoc doc = new HTMLDoc("http://wikipedia.com/wiki/Special:Random");
		if (doc == null || doc.getDoc() == null) return;
		Elements article = doc.getDoc().select("#mw-content-text");
		String[] wds = article.get(0).text().split("\\W+");
		for (String s : wds) {
			int ind = words.indexOf(s.toLowerCase());
			if (ind != -1) {
				occurrences.set(ind, occurrences.get(ind) + 1);
				while (ind > 0 && occurrences.get(ind - 1) < occurrences.get(ind)) {
					int temp = occurrences.get(ind - 1);
					occurrences.set(ind - 1, occurrences.get(ind));
					occurrences.set(ind, temp);
					String temp2 = words.get(ind - 1);
					words.set(ind - 1, words.get(ind));
					words.set(ind, temp2);
					ind--;
					if (ind <= 20) {
						repaint();
					}
				}
			}
		}
		repaint();
	}

	public void populateList() {
		Scanner scan = null;
		try {
			scan = new Scanner(new File("wordlist.txt"));
		} catch (Exception e) {

		}
		while (scan.hasNext()) {
			String line = scan.nextLine();
			words.add(line);
			constants.add(line);
		}
		for (int i = 0; i < words.size(); i++) {
			occurrences.add(0);
		}
	}

	public int indexOf(String str) {
		return indexOf(str, 0, constants.size() - 1);
	}

	public int indexOf(String str, int from, int to) {
		int length = to - from;
		int index = from + length / 2;
		if (constants.get(index).equals(str)) return index;
		if (to <= from) return -1;
		else if (constants.get(index).compareTo(str) > 0) return indexOf(str, from, index - 1);
		else return indexOf(str, index + 1, to);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.BLACK);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("Number of articles checked: " + numArticles, 300, 660);
		g.drawString("Time elapsed: " + Math.round((System.currentTimeMillis() - start)) / 1000.0 + " s", 300, 630);
		if (words.size() == 0 || occurrences.size() == 0) return;
		for (int i = 0; i < 20; i++) {
			g.drawString(words.get(i) + " - " + occurrences.get(i) + " occurrences", 300, 30 * i + 30);
		}
	}
}
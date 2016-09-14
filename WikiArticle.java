import java.util.ArrayList;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.File;

public class WikiArticle {

	private String content;
	private String title;
	private String description;
	
	public WikiArticle() {
		HTMLDoc doc = new HTMLDoc("http://wikipedia.com/wiki/Special:Random");
		if (!doc.isValid()) {
			return;
		}
		title = doc.getDivTextById("firstHeading");
		content = doc.getDivTextById("mw-content-text");
		try {
			description = doc.getDoc().select("#mw-content-text p").get(0).text();
		} catch (Exception e) {
			
		}
	}

	public WikiArticle(String search) {
		HTMLDoc doc = new HTMLDoc("http://wikipedia.com/wiki/" + search);
		if (!doc.isValid()) {
			return;
		}
		title = doc.getDivTextById("firstHeading");
		content = doc.getDivTextById("mw-content-text");
		String temp1 = " is ";
		String temp2 = " was ";
		int index1 = content.indexOf(temp1);
		int index2 = content.indexOf(temp2);
		if (index2 != -1 && index2 < index1) {
			index1 = index2;
			temp1 = new String(temp2);
		}
		if (index1 == -1) description = "";
		else {
			description = content.substring(index1 + temp1.length(), content.indexOf(".", index1));
		}
	}

	public WikiArticle(String title, String description, String content) {
		this.title = title;
		this.description = description;
		this.content = content;
	}

	public boolean exists() {
		return content != null;
	}

	public String toString() {
		return title + ": " + description;
	}

	public String getContent() {
		return content;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public boolean hasDescription() {
		return !(description.equals(""));
	}

	public void findKeyWords(ArrayList<String> wanted, ArrayList<String> unwanted, ArrayList<String> matches, PrintWriter pw) {
		ArrayList<String> common = new ArrayList<String>();
		String[] words1 = description.split("\\W+");
		for (int i = 0; i < words1.length; i++) {
			String word = words1[i];
			if (unwanted.indexOf(word) != -1) continue;
			if (wanted.indexOf(word) == -1) {
				if (!isKeyWord(word, unwanted)) {
					pw.println(word);
					unwanted.add(word);
					continue;
				}
				else {
					// System.out.println(word);
					wanted.add(word);
				}
			}
			common.add(word);
		}
		pw.close();
		for (String s : common) {
			if (matches.indexOf(s) == -1) matches.add(s);
		}
	}

	public boolean isKeyWord(String str, ArrayList<String> unwanted) {
		if (str.length() < 4) return false;
		if (containsNumbers(str)) return false;
		if (str.endsWith("ed")) return false;
		if (unwanted.indexOf(str) != -1) return false;
		return true;
	}

	public boolean containsNumbers(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) >= '0' && str.charAt(i) <= '9') return true;
		}
		return false;
	}

	public ArrayList<String> categorize() {
		Scanner scan = null;
		try {
			scan = new Scanner(new File("wanted.txt"));
		} catch (Exception e) {
			System.err.println("ERROR: Could not find wanted.txt");
			System.exit(1);
		}
		ArrayList<String> categories = new ArrayList<String>();
		ArrayList<String> matches = new ArrayList<String>();
		while (scan.hasNext()) {
			categories.add(scan.nextLine());
		}
		String[] words = description.split("\\W+");
		for (String s : words) {
			if (categories.indexOf(s) != -1) matches.add(s);
		}
		return matches;
	}

	public int find(String str, String[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(str)) return i;
		}
		return -1;
	}
}
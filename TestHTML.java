import java.util.ArrayList;
import java.util.Scanner;

public class TestHTML {
	public static void main(String[] args) {
		ArrayList<String> words = new ArrayList<String>();
		String srch = "";
		try {
			srch = args[0];
		} catch (Exception e) {
			Scanner kb = new Scanner(System.in);
			System.out.print("Enter an initial search -> ");
			srch = kb.nextLine();
		}
		for (int i = 0; i < 1000; i++) {
			HTMLDoc doc = new HTMLDoc("http://www.urbandictionary.com/define.php?term=" + srch);
			if (doc == null || doc.getDoc() == null) continue;
			String article = doc.getAllText();
			String[] wds = article.split("\\W+");
			for (String s : wds) {
				if (s.length() == 0 || words.indexOf(s) != -1) continue;
				char c = s.charAt(0);
				if (c >= 'a' && c <= 'z') {
					words.add(s);
				}
			}
			srch = words.get((int)(Math.random() * words.size()));
			System.out.println(words.size() + " words, new search: " + srch);
		}
	}
}
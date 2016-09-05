import java.util.ArrayList;
import java.util.Scanner;

public class CategorizeArticle {
	public static void main(String[] args) {
		String search = "";
		try {
			search = args[0];
		} catch (Exception e) {
			Scanner s = new Scanner(System.in);
			System.out.print("Enter a search -> ");
			search = s.nextLine();
		}
		for (int i = 0; i < search.length(); i++) {
			if (search.charAt(i) == ' ') {
				search = search.substring(0, i) + "_" + search.substring(i + 1);
			}
		}
		if (search.equals("random")) search = "Special:Random";
		WikiArticle wa = new WikiArticle(search);
		ArrayList<String> cats = wa.categorize();
		System.out.println(wa.getTitle() + ": " + cats);
	}
}
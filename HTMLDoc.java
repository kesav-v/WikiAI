import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.helper.*;
import org.jsoup.select.*;

public class HTMLDoc {

	private Document doc;
	private boolean valid = true;

	public HTMLDoc(String s) {
		create(s);
	}

	private void create(String url) {
		try {
			doc = Jsoup.connect(url).get();
		} catch (Exception e) {
			System.out.println(e);
			// System.err.println("ERROR: Invalid URL " + url);
			valid = false;
		}
	}

	public boolean isValid() {
		return valid;
	}

	public Document getDoc() {
		return doc;
	}

	public String getDivTextById(String divName) {
		return doc.select("#" + divName).get(0).text();
	}

	public String[] getAllLinks() {
		Elements hrefs = doc.select("a[href]");
		String[] links = new String[hrefs.size()];
		for (int i = 0; i < hrefs.size(); i++) {
			links[i] = hrefs.get(i).attr("abs:href");
		}
		return links;
	}

	public String getAllText() {
		return doc.text();
	}
}
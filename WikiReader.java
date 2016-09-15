public class WikiReader {

	private long start;
	private WikiThread[] threads;

	public WikiReader(int seconds, int len) {
		start = System.nanoTime();
		threads = new WikiThread[len];
		int count = 1;
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new WikiThread(seconds, this, start);
			threads[i].start();
			count++;
		}
	}

	public static void main(String[] args) {
		WikiReader reader = new WikiReader(30, 70);
	}

	public double timeElapsed() {
		return (System.nanoTime() - start) / 1E9;
	}

	public synchronized int read() {
		WikiArticle article = new WikiArticle();
		if (!article.exists()) return -1;
		return (article.getContent().split("\\W+").length);
	}

	public void printArticles() {
		int sum = 0;
		for (WikiThread wt : threads) {
			sum += wt.numArticles();
		}
		System.out.println(threads.length + "\t" + sum);
	}
}
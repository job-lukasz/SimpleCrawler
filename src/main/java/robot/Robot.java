package robot;


public class Robot {
	

	public static void main(String[] args) {
		Crawler crawler = new Crawler("http://artemis.wszib.edu.pl/~lujob");
		int i = 0;
		while (i < 50) {
			crawler.nextIteration();
			i++;
		}
		crawler.showWordSet();
	}
}

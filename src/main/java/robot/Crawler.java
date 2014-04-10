package robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Crawler {

	private String source;
	private Set<String> linksSet;
	private Map<String, Integer> wordsMap;
	private int iteration;

	public Crawler(String source) {
		super();
		this.source = source;
		this.linksSet = new LinkedHashSet<String>();
		linksSet.add(source);
		this.wordsMap = new TreeMap<String, Integer>();
	}

	private String getSite(String link) throws MalformedURLException,
			IOException {
		String source = "";
		URL akt = new URL(link);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				akt.openStream(), "utf-8"));
		String linia;
		while ((linia = in.readLine()) != null) {
			source += linia+"\n";
		}
		in.close();
		return source;
	}

	private void getWords(String site) {
		String text[] = site.split("<[^/?].*>");//rozdzielenie po <jakis znacznik i atrybuty> ale nie </
		String rawText = "";
		for (int i = 0; i < text.length; i++) {
			String temp[] = text[i].split("</.*>");
			rawText += temp[0];
		}
		String[] words = rawText.split("\\s+"); // rozdzielenie po białych znakach
		for (String rawWord : words) {
			String word = rawWord.replaceAll("[^\\p{L}]", ""); // zamiania wszystkich znaków nie bedacych literami
			word = word.toLowerCase();
			if (word.compareTo("") != 0) {
				if (wordsMap.containsKey(word)) {
					wordsMap.put(word, wordsMap.get(word) + 1);
				} else {
					wordsMap.put(word, 1);
				}
			}
		}
	}

	private String[] getLinks(String site) {
		String links[] = site.split("<a.* href=\"");
		for (int i = 1; i < links.length; i++) {
			links[i] = links[i].split("\"")[0];
			links[i] = getLinkSource(links[i]);
			if (linksSet.add(links[i])) {
				System.out.println("dodano link: " + links[i]);
			}
		}
		return links;
	}

	private String getLinkSource(String link) {
		if (link.startsWith("http://")) {
			return link;
		} else if (link.matches(".*(.html|.php|.htm).*")) {
			if (source.endsWith("/")) {
				return source + link;
			} else {
				return source + "/" + link;
			}
		}
		return "";
	}

	public void goToNextSite() {
		if (iteration < linksSet.size()) {
			try {
				String link = (String) linksSet.toArray()[iteration];
				System.out.println("Przechodzę na stronę: " + link);
				String site = getSite(link);
				getLinks(site);
				getWords(site);
			} catch (MalformedURLException ex) {
				System.out.println("nirozpoznany typ odnosnika");
			} catch (IOException ex) {
				System.out.println("nieobsługnany odnosnik");
			}
			iteration++;
		}
	}

	public void printWordSet() {
		Set<String> words1 = wordsMap.keySet();
		Iterator<String> word = words1.iterator();
		while (word.hasNext()) {
			String tmp = word.next();
			System.out.println(tmp + ": " + wordsMap.get(tmp));
		}
	}
}

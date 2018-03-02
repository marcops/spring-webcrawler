package org.marco.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.marco.convert.LinkConverter;
import org.marco.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebCrawlerService {
	private HashMap<String, Link> links = new HashMap<>();
	
	@Value("${webcrawler.timeout:5000}")
	private Integer TIMEOUT_IN_MS;
	
	@Autowired
	private LinkConverter linkConverter;

	public List<Link> execute(String domain) throws Exception  {
		getPageLinks(domain, new URL(domain)).get();
		return links
				.values()
				.stream()
				.collect(Collectors.toList());
	}
	
	private CompletableFuture<Link> getPageLinks(String url, URL domain) {
		return CompletableFuture.supplyAsync(()-> asyncPageLinks(url, domain));
	}

	private Link asyncPageLinks(String url, URL domain) {
		try {
			if (links.containsKey(url) || !isInternalUrl(url, domain)) return null;
			Link link = getLink(url, domain);
			links.put(url, link);
			getLinksChildren(domain, link);
			return link;
		} catch (IOException | InterruptedException | ExecutionException e) {
			System.err.println("For '" + url + "': " + e.getMessage());
			return null;
		}
	}

	private void getLinksChildren(URL domain, Link link) throws InterruptedException, ExecutionException {
		List<CompletableFuture<Link>> futuristicPage = link.getChildrens()
				.stream()
				.map(x->getPageLinks(x, domain))
				.collect(Collectors.toList());
		
		CompletableFuture.allOf(futuristicPage.toArray(new CompletableFuture[futuristicPage.size()])).get();
	}

	private Link getLink(String url, URL domain) throws IOException {
		Response response = Jsoup.connect(url).timeout(TIMEOUT_IN_MS).execute();
		Document document = response.parse();
		Link link = linkConverter.from(document, response);
		if (isInternalUrl(url, domain)) link.setChildrens(getChildrens(document));
		return link;
	}

	private boolean isInternalUrl(String page, URL domain) throws MalformedURLException {
		URL url = new URL(page);
		return url.getHost().equals(domain.getHost());
	}

	private Set<String> getChildrens(Document document) {
		return document.select("a[href]")
				.stream()
				.map(x -> getUrlClean(x.absUrl("href")))
				.filter(x -> x != null)
				.collect(Collectors.toSet());
	}

	private String getUrlClean(String page) {
		try {
			URL url = new URL(page);
			return url.getProtocol() + "://" + url.getHost() + url.getPath();
		} catch (MalformedURLException e) {
			return null;
		}
	}
}

package org.marco.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.marco.builder.LinkBuilder;
import org.marco.model.Link;
import org.springframework.stereotype.Service;

@Service
public class NavigateService {
	private HashMap<String, Link> links = new HashMap<>();

	public List<Link> execute(String domain) throws Exception  {
		getPageLinks(domain, new URL(domain)).get();
		return links.values()
				.stream()
				.collect(Collectors.toList());
	}
	
	private CompletableFuture<Link> getPageLinks(String url, URL domain) {
		return CompletableFuture.supplyAsync(()-> asyncPageLinks(url, domain));
	}

	private Link asyncPageLinks(String url, URL domain) {
		try {
			if (links.containsKey(url) || !isInternalUrl(url, domain)) return null;
			
			Response response = Jsoup.connect(url).execute();
			Document document = response.parse();
			
			Link link = LinkBuilder.builder()
					.url(document.location())
					.title(document.title())
					.lastModified(response.header("Last-Modified"))
					.childrens(isInternalUrl(url, domain) ? getChildrens(document) : new HashSet<String>())
					.build();
			
			links.put(url, link);
			processLinksChildren(domain, link);
			return link;
		} catch (Exception e) {
			System.err.println("For '" + url + "': " + e.getMessage());
			return null;
		}
	}

	private void processLinksChildren(URL domain, Link link) throws Exception {
		List<CompletableFuture<Link>> futuristicPage = link.getChildrens()
				.stream()
				.map(x->getPageLinks(x, domain))
				.collect(Collectors.toList());
		
		CompletableFuture.allOf(futuristicPage.toArray(new CompletableFuture[futuristicPage.size()])).get();
	}

	private boolean isInternalUrl(String page, URL domain) throws MalformedURLException {
		return new URL(page).getHost().equals(domain.getHost());
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

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
import org.springframework.stereotype.Service;

@Service
public class WebCrawlerService {
	private HashMap<String, Link> links = new HashMap<>();

	@Autowired
	private LinkConverter linkConverter;

	public List<Link> execute(String domain) throws InterruptedException, ExecutionException {
		CompletableFuture <Link> cf = getPageLinks(domain, domain);
		cf.get();
		return links
				.values()
				.stream()
				.collect(Collectors.toList());
		
	}
	
	
	private CompletableFuture<Link> getPageLinks(String url, String domain) {
		return CompletableFuture.supplyAsync(() -> {
			if (links.containsKey(url)) return null;
			try {
				Response con =  Jsoup.connect(url).timeout(5000).execute();
				Document document =  con.parse();
				Link link = linkConverter.from(document, con);
				
				if(isInternalUrl(url, domain)) link.setChildrens(getChildrens(document));
	
				links.put(url, link);
				
				if(link != null) {
					List<CompletableFuture<Link>> pageContentFutures = 
							link.getChildrens().stream().map(x->getPageLinks(x, domain)).collect(Collectors.toList());
					
					CompletableFuture<Void> allFutures = CompletableFuture.allOf(
					        pageContentFutures.toArray(new CompletableFuture[pageContentFutures.size()]));
					allFutures.get();
				}
				        
				return link;
			} catch (IOException | InterruptedException | ExecutionException e) {
				System.err.println("For '" + url + "': " + e.getMessage());
				return null;
			}
		});
	}

	private boolean isInternalUrl(String url, String domain) {
		return url.contains(domain);
	}

	private Set<String> getChildrens(Document document) {
		return document.select("a[href]")
				.stream()
				.map(x->getUrlClean(x.absUrl("abs:href")))
				.filter(x->x!=null)
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

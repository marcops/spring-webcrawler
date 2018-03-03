package org.marco.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.marco.builder.LinkBuilder;
import org.marco.model.Link;
import org.springframework.stereotype.Service;

@Service
public class NavigateService {
	private Set<String> links = new HashSet<>();
	
	/**
     * Discover all pages related with domain and export it 
     * in a list content {@link Link} information
     *
     * @param   domain
     *          the url domain to discover all pages related
     *
     * @return  A list content {@link Link} information
     *
     * @throws  Exception
     *          if an error occurs an exception will be handle
     */
	public List<Link> execute(String domain) throws Exception  {
		return getPageLinks(domain, new URL(domain)).get();
	}
	
	private CompletableFuture<List<Link>> getPageLinks(String url, URL domain) {
		return CompletableFuture.supplyAsync(()-> asyncPageLinks(url, domain));
	}

	private List<Link> asyncPageLinks(String url, URL domain) {
		try {
			if (links.contains(url) || !isInternalUrl(url, domain)) return new ArrayList<>();
			links.add(url);
			
			Response response = Jsoup.connect(url).execute();
			Document document = response.parse();
			
			Link link = LinkBuilder.builder()
					.url(document.location())
					.title(document.title())
					.lastModified(response.header("Last-Modified"))
					.childrens(isInternalUrl(url, domain) ? getChildrens(document) : new HashSet<String>())
					.build();
			
			return Stream
					.concat(Stream.of(link), processLinksChildren(domain, link).stream())
					.collect(Collectors.toList());
		} catch (Exception e) {
			System.err.println("For '" + url + "': " + e.getMessage());
			return new ArrayList<>();
		}
	}

	private List<Link> processLinksChildren(URL domain, Link link) throws Exception {
		List<CompletableFuture<List<Link>>> futuristicPage = link.getChildrens()
				.stream()
				.map(x->getPageLinks(x, domain))
				.collect(Collectors.toList());
		//wait all async process closed
		CompletableFuture.allOf(futuristicPage.toArray(new CompletableFuture[futuristicPage.size()])).get();
		
		return futuristicPage.stream()
				  .map(CompletableFuture::join)
				  .flatMap(listContainer -> listContainer.stream())
				  .collect(Collectors.toList());
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

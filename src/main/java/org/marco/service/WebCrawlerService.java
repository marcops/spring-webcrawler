package org.marco.service;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.marco.convert.LinkConverter;
import org.marco.model.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebCrawlerService {
	private HashMap<String, Link> links = new HashMap<>();

	@Autowired
	private LinkConverter linkConverter;

	public List<Link> execute(String url) {
		getPageLinks(url, url);
		return links
				.values()
				.stream()
				.collect(Collectors.toList());
	}

	
//	@Async
	private void getPageLinks(String url, String domain) {
		if (!links.containsKey(url)) {
			try {
				Response con =  Jsoup.connect(url).timeout(5000).execute();
				Document document =  con.parse();
				Link link = linkConverter.from(document, con);
				
				if(url.contains(domain)) {
					Elements linksOnPage = document.select("a[href]");
					for (Element page : linksOnPage) {
						//remove parameters and others
						URL urlClean = new URL(page.absUrl("abs:href"));
						link.getChildrens().add(urlClean.getProtocol() +"://"+ urlClean.getHost() +  urlClean.getPath());
					}
				}
				links.put(url, link);
				
				if(link.getChildrens()!=null) {
					link.getChildrens().stream().forEach(x->getPageLinks(x, domain));
				}
			} catch (IOException e) {
				System.err.println("For '" + url + "': " + e.getMessage());
			}
		}
	}
}

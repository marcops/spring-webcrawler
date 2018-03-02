package org.marco.service;

import java.io.IOException;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class WebCrawlerService {
	private HashSet<String> links;
	private String domain;
	public WebCrawlerService() {
		links = new HashSet<String>();
	}
	
	public void execute(String url) {
		domain = url;
		getPageLinks(url);
	}

	private void getPageLinks(String url) {
		if (!links.contains(url)) {
			System.out.println(url);
			try {
				links.add(url);

				Document document = Jsoup.connect(url).get();
				Elements linksOnPage = document.select("a[href]");

				for (Element page : linksOnPage) {
					String nextUrl = page.attr("abs:href");
					if(nextUrl.contains(domain)) getPageLinks(nextUrl);
					else if (!links.contains(nextUrl)) {
						System.out.println(nextUrl);
						links.add(nextUrl);
					}
				}
			} catch (IOException e) {
				System.err.println("For '" + url + "': " + e.getMessage());
			}
		}
	}
}

package org.marco.convert;

import java.util.HashSet;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.marco.model.Link;
import org.springframework.stereotype.Component;

@Component
public class LinkConverter {
	public Link from(Document document, Response response) {
		Link link = new Link();
		link.setTitle(document.title());
		link.setUrl(document.location());
		link.setLastModified(response.header("Last-Modified"));
		link.setChildrens(new HashSet<>());
		return link;
	}
}

package org.marco.builder;

import java.util.HashSet;
import java.util.Set;

import org.marco.model.Link;

public class LinkBuilder {
	private Link link;

	private LinkBuilder() {
		this.link = new Link();
		this.link.setChildrens(new HashSet<String>());
	}

	public static LinkBuilder builder() {
		return new LinkBuilder();
	}

	public Link build() {
		return this.link;
	}

	public LinkBuilder title(String title) {
		this.link.setTitle(title);
		return this;
	}

	public LinkBuilder url(String url) {
		this.link.setUrl(url);
		return this;
	}

	public LinkBuilder lastModified(String lastModified) {
		this.link.setLastModified(lastModified);
		return this;
	}

	public LinkBuilder childrens(Set<String> childrens) {
		this.link.setChildrens(childrens);
		return this;
	}
}

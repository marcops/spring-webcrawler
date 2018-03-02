package org.marco.model;

import java.util.Set;

public class Link {
	private String url;
	private String title;
	private String lastModified;
	private Set<String> childrens;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<String> getChildrens() {
		return childrens;
	}

	public void setChildrens(Set<String> set) {
		this.childrens = set;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public String toString() {
		return "Link [url=" + url + ", title=" + title + ", lastModified=" + lastModified + "]";
	}

}

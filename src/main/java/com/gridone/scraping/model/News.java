package com.gridone.scraping.model;

public class News {
	private String title;
	private String link;
	private String content;
	
	public News(String title,String link,String content) {
		this.title = title;
		this.link = link;
		this.content = content;
	}
	
	public String getTitle() {
		return title;
	}
	public String getLink() {
		return link;
	}
	public String getContent() {
		return content;
	}
}

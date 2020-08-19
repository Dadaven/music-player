package com.example.entity;

public class News {

	private String title;
	private String link;
	private String author;
	private String pubDate;
	private String description;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
//	public String getGuid() {
//		return guid;
//	}
//	public void setGuid(String guid) {
//		this.guid = guid;
//	}
//	public News(String title, String link, String pubDate, String description,
//			String guid) {
//		super();
//		this.title = title;
//		this.link = link;
//		this.pubDate = pubDate;
//		this.description = description;
//		this.guid = guid;
//	}
	
	public News(){
		
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public News(String title, String link, String author, String pubDate,
			String description) {
		super();
		this.title = title;
		this.link = link;
		this.author = author;
		this.pubDate = pubDate;
		this.description = description;
	}
	
}

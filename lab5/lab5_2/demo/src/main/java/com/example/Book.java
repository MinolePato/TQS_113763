package com.example;
 
import java.util.Date;

public class Book {
	private final String title;
	private final String author;
	private final Date published;

	public Book(final String title, final String author, final int year) {
		this.title = title;
		this.author = author;

		final Date date = new Date();
		date.setYear(year);
		this.published = date;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public Date getPublished() {
		return published;
	}

	@Override
	public String toString() {
		return "Book [title=" + title + ", author=" + author + ", published=" + published + "]";
	}
}
package com.nexmo.spock.book.catalog.dao;

import com.nexmo.spock.book.catalog.model.Book;

import java.util.List;

public interface BookCatalogDao {

    Book addBook(Book book);
    Book updateBook(Book book);
    Book getBook(String isbn);
    List<Book> getBooks();
    void removeBook(String isbn);
}

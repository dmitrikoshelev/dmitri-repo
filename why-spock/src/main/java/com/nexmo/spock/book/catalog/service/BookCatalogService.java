package com.nexmo.spock.book.catalog.service;

import com.nexmo.spock.book.catalog.dao.BookCatalogDao;
import com.nexmo.spock.book.catalog.model.Book;

import java.util.List;

public class BookCatalogService {

    BookCatalogDao catalogDao;

    public BookCatalogService(BookCatalogDao catalogDao) {
        this.catalogDao = catalogDao;
    }

    public Book getBookById(String isbn) {
        return catalogDao.getBook(isbn);
    }

    public List<Book> getAllBooks() {
        return catalogDao.getBooks();
    }

    public Book saveOrUpdateBook(Book book) {
        Book aBook = catalogDao.getBook(book.getIsbn());

        if(aBook == null) {
            return catalogDao.addBook(book);
        } else {
            return catalogDao.updateBook(book);
        }
    }

    public void deleteBook(String isbn) {
        Book aBook = catalogDao.getBook(isbn);

        if(aBook == null) {
            throw new IllegalArgumentException(String.format("Book with isbn '%s' was not found in catalog", isbn));
        }
        catalogDao.removeBook(isbn);
    }

}

package com.nexmo.spock.book.catalog.service

import com.nexmo.spock.book.catalog.dao.BookCatalogDao
import com.nexmo.spock.book.catalog.model.Book
import spock.lang.Specification
import spock.lang.Subject

@Subject(BookCatalogService.class)
public class BookCatalogServiceSpeck extends Specification {

    BookCatalogService catalogService
    BookCatalogDao catalogDao

    def setup() {
        catalogDao = Mock(BookCatalogDao)
        catalogService = new BookCatalogService(catalogDao)
    }

    def "Should successfully retrieve a book from catalog"() {
        given:"book in catalog"
        def book = new Book(title: "Learning Spock",
                            author: "Kostis Kapelonis",
                            isbn: "1234-5678-9000",
                            price: 35.55)
        catalogDao.getBook(_) >> book
        when:"isbn of existing book is used"
        def aBook = catalogService.getBookById("1234-5678-9000")

        then:"that book is found in catalog"
        with(aBook) {
            isbn == book.isbn
            author == book.author
            title == book.title
            price == book.price
        }
    }

    def "Should successfully retrieve books from catalog"() {
        given:"book in catalog"
        def b1 = new Book()
        b1.with {
            title: "Learning Spock"
            author: "Kostis Kapelonis"
            isbn: "3456-5678-9000"
            price: 35.55
        }
        and:"another book"
        def b2 = new Book()
        b2.with {
            title: "Learning Groovy"
            author: "Kostis Kapelonis"
            isbn: "1234-5678-7000"
        }

        catalogDao.getBooks() >> [b1, b2]

        when:"catalog is queried for all books"
        def allBooks = catalogService.getAllBooks()

        then:"all books are returned from catalog"
        with(allBooks){
            size == 2
        }
    }

    def "should successfully add or update a book in the catalog"() {
        given:"dummy data from the interface"
        def b1 = new Book()
        catalogDao.getBook(_) >>> [null, b1]

        when:"we try to update a book"
        catalogService.saveOrUpdateBook(b1)

        then:"book is added"
        1 * catalogDao.addBook(b1)

        when:"we try to update a book"
        catalogService.saveOrUpdateBook(b1)

        then:"book is updated"
        1 * catalogDao.updateBook(b1)
    }

    def "Should succeed to remove a book from catalog"() {
        given:"book in a catalog"
        def b1 = new Book(isbn: "3456-5678-9000")
        catalogDao.getBook(_) >> b1

        when:"we try to delete a book"
        catalogService.deleteBook("3456-5678-9000")

        then:"book for a given isbn is removed from a catalog"
        1 * catalogDao.removeBook("3456-5678-9000")
    }

    def "Should fail to remove a book from catalog"() {
        given:"not existent book"
        catalogDao.getBook("some_id") >> null

        when:"we try to delete a book"
        catalogService.deleteBook("some_id")

        then:"exception is thrown"
        IllegalArgumentException e = thrown()
        e.message == "Book with isbn 'some_id' was not found in catalog"
    }
}

package com.example.lms.service;

import com.example.lms.errorHandling.GlobalNotFoundException;
import com.example.lms.model.Author;
import com.example.lms.model.Book;
import com.example.lms.repositories.AuthorRepository;
import com.example.lms.repositories.BookRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class BookServices {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;



    @GetMapping
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Book getOneBook(@PathVariable Long id){
        if(bookRepository.findByBookId(id)==null){
            throw new GlobalNotFoundException("Book does not exist.");
        }
        return bookRepository.findByBookId(id);
    }


    @PostMapping
    public Book createBookDetails(@RequestBody final Book book){
        Author author=authorRepository.findByAuthorId(book.getAuthor().getAuthorId());
        if(author==null){
            authorRepository.save(book.getAuthor());
        }
        book.setAuthor(author);
        return bookRepository.saveAndFlush(book);
    }


    // Deleting a book using its id
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public String deleteOneBook(@PathVariable Long id){
        Book book=bookRepository.findByBookId(id);
        if(book==null){
            return "Book id not found";
        }
        bookRepository.deleteById(id);
        return "Book is deleted successfully";
    }


    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public Book updateBookDetails(@PathVariable Long id, @RequestBody Book bookDetails) {
        Book existingBookDetails = bookRepository.findById(id).orElseThrow(() -> new GlobalNotFoundException("Book not found"));
        Author author = bookDetails.getAuthor();
        if (author != null) {
            if (author.getAuthorId() != null) {
                Author existingAuthor = authorRepository.findById(author.getAuthorId()).orElseThrow(() -> new GlobalNotFoundException("Author not found"));
                existingAuthor.setAuthorName(author.getAuthorName());
                existingAuthor.setAuthorBio(author.getAuthorBio());
                authorRepository.saveAndFlush(existingAuthor);
                existingBookDetails.setAuthor(existingAuthor);
            } else {
                Author newAuthor = new Author();
                newAuthor.setAuthorName(author.getAuthorName());
                newAuthor.setAuthorBio(author.getAuthorBio());
                authorRepository.saveAndFlush(newAuthor);
                existingBookDetails.setAuthor(newAuthor);
            }
        }
        BeanUtils.copyProperties(bookDetails, existingBookDetails, "bookId");
        return bookRepository.saveAndFlush(existingBookDetails);
    }

}

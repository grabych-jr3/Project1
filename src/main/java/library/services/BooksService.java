package library.services;

import library.models.Book;
import library.models.Person;
import library.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> index(){
        return booksRepository.findAll();
    }

    public List<Book> index(Integer page, Integer booksPerPage){
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public List<Book> index(boolean sort_by_year){
        return booksRepository.findAll(Sort.by("year"));
    }

    public List<Book> index(Integer page, Integer booksPerPage, boolean sort_by_year){
        return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
    }

    public Book show(int id){
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void create(Book book){
        booksRepository.save(book);
    }

    @Transactional
    public void edit(int id, Book updatedBook){
        updatedBook.setBookId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    public List<Book> findByOwnerPersonId(int id){
        List<Book> books = booksRepository.findByOwnerPersonId(id);
        for(Book book : books){
            if(book.getAssignedAt() != null && Duration.between(book.getAssignedAt(), LocalDateTime.now()).toDays() > 10){
                book.setExpired(true);
            }
        }
        return books;
    }

    @Transactional
    public void clearOwnerByBookId(int id){
        Book book = booksRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        Person owner = book.getOwner();

        if(owner != null){
            owner.getBooks().remove(book);
            book.setOwner(null);
        }
        book.setAssignedAt(null);
    }

    @Transactional
    public void setOwnerByBookId(int id, Person person){
        Book book = booksRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
        book.setOwner(person);
        person.getBooks().add(book);
        book.setAssignedAt(LocalDateTime.now());
    }

    public Person findOwnerByBookId(int id){
        Optional<Book> book = booksRepository.findById(id);
        return book.map(Book::getOwner).orElse(null);
    }

    public List<Book> searchAllBooksByName(String name){
        return booksRepository.findByNameStartingWithIgnoreCase(name);
    }
}

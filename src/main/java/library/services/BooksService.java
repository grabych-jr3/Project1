package library.services;

import library.models.Book;
import library.models.Person;
import library.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public List<Book> findByOwnerPersonId(int id){
        return booksRepository.findByOwnerPersonId(id);
    }

    @Transactional
    public void clearOwnerByBookId(int id){
        booksRepository.clearOwnerByBookId(id);
    }

    @Transactional
    public void setOwnerByBookId(int id, int personId){
        booksRepository.setOwnerByBookId(id, personId);
    }

    public Person findOwnerByBookId(int id){
        Optional<Book> book = booksRepository.findById(id);
        return book.map(Book::getOwner).orElse(null);
    }
}

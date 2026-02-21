package library.dao;

import library.models.Book;
import library.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index(){
        return jdbcTemplate.query("SELECT * FROM book ORDER BY book_id", new BeanPropertyRowMapper<>(Book.class));
    }

    public Person showPersonWhoTookTheBook(int id){
        return jdbcTemplate.query("select * from person where person_id = (select person_id from book where book_id = ?)", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }

    public Book show(int id){
        return jdbcTemplate.query("SELECT * FROM book WHERE book_id = ?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class)).stream().findAny().orElse(null);
    }

    public void create(Book book){
        jdbcTemplate.update("INSERT INTO book(name, author, year) VALUES (?, ?, ?)", book.getName(), book.getAuthor(), book.getYear());
    }

    public void edit(int id, Book updatedBook){
        jdbcTemplate.update("UPDATE book SET name=?, author=?, year=? WHERE book_id = ?", updatedBook.getName(), updatedBook.getAuthor(),
                updatedBook.getYear(), id);
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE FROM book WHERE book_id = ?", id);
    }

    public void deleteBookFromPerson(int id){
        jdbcTemplate.update("UPDATE book SET person_id = NULL WHERE book_id=?", id);
    }

    public void assignBook(int bookId, int personId){
        jdbcTemplate.update("UPDATE book SET person_id = ? WHERE book_id = ?", personId, bookId);
    }
}

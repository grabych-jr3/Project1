package library.dao;

import library.models.Book;
import library.models.Person;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index(){
        return jdbcTemplate.query("SELECT * FROM person ORDER BY person_id", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id){
        return jdbcTemplate.query("SELECT * FROM person WHERE person_id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }

    public List<Book> getBooksByUserId(int id){
        return jdbcTemplate.query("SELECT * FROM person p\n" +
                "JOIN public.book b on p.person_id = b.person_id\n" +
                "WHERE b.person_id = ?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class));
    }

    public Optional<Person> show(String fullName){
        return jdbcTemplate.query("SELECT * FROM person WHERE fullname = ?", new Object[]{fullName}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public void create(Person person){
        jdbcTemplate.update("INSERT INTO person(fullname, birth_year) VALUES (?, ?)", person.getFullName(), person.getBirthYear());
    }

    public void edit(int id, Person updatedPerson){
        jdbcTemplate.update("UPDATE person SET fullname=?, birth_year=? WHERE person_id = ?", updatedPerson.getFullName(), updatedPerson.getBirthYear(), id);
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE FROM person WHERE person_id = ?", id);
    }
}

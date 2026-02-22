package library.repositories;

import library.models.Book;
import library.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwnerPersonId(int id);

    @Modifying
    @Transactional
    @Query("update Book b set b.owner=null where b.bookId = :id")
    void clearOwnerByBookId(@Param("id") int id);

    @Modifying
    @Transactional
    @Query("update Book b set b.owner.personId = :personId where b.bookId = :id")
    void setOwnerByBookId(@Param("id") int id, @Param("personId") int personId);

    @Modifying
    @Transactional
    @Query("update Book b set b.assignedAt = CURRENT_TIMESTAMP where b.bookId = :id")
    void setAssignedAt(@Param("id") int id);

    @Modifying
    @Transactional
    @Query("update Book b set b.assignedAt = null where b.bookId = :id")
    void clearAssignedAt(@Param("id") int id);

    List<Book> findByNameLike(String name);
}

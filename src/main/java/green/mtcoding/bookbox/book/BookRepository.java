package green.mtcoding.bookbox.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {

    @Query("SELECT b FROM Book b WHERE b.title LIKE %:title% OR b.author LIKE %:author% OR b.publisher LIKE %:publisher% order by b.isbn13 desc")
    List<Book> mFindAll(@Param("title") String title, @Param("author") String author, @Param("publisher") String publisher);
}

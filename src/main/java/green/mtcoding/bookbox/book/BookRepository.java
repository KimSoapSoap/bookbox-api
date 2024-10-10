package green.mtcoding.bookbox.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {
    //검색으로 찾는 것
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:searchTerm% OR b.author LIKE %:searchTerm% OR b.publisher LIKE %:searchTerm%")
    List<Book> mFindAll(@Param("searchTerm") String searchTerm);
    //메인에 보일 것
    @Query("select b from Book b left join fetch b.category c")
    List<Book> mFindAllWithCategory();


}

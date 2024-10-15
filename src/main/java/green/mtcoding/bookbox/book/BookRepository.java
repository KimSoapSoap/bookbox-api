package green.mtcoding.bookbox.book;

import green.mtcoding.bookbox.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, String> {
    //검색으로 찾는 것
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:searchTerm% OR b.author LIKE %:searchTerm% OR b.publisher LIKE %:searchTerm%")
    List<Book> mFindAll(@Param("searchTerm") String searchTerm);
    //메인에 책만 보일 것
    @Query("select b from Book b left join fetch b.category c")
    List<Book> mFindBooksWithCategory();
    //메인에 보일 것
    @Query("select b from Category b left join fetch b.books c")
    List<Category> mFindAllWithCategory();

    //카테고리 누르면 해당 책들 나올 수 있게 만들기
    @Query("select b from Book b where b.category.id =:id")
    List<Book> mFindByCategoryId(@Param("id") String id);

    //상세보기
//    @Query("select b from Book b join fetch b.comments c where b.isbn13=:isbn13")
//    Optional<Book> mFindByIdWithComment(@Param("id") String isbn13);


    // 대여 상태인지 확인
    @Query("select b.lendStatus from Book b where b.isbn13 = :isbn13")
    Optional<Boolean> mCheckLendStatus(@Param("isbn13") String isbn13);

    // 해당 북 대여상태로 바꾸기
    @Modifying
    @Query("UPDATE Book b SET b.lendStatus = true, b.lendCount = b.lendCount + 1 WHERE b.isbn13 = :isbn13")
    Integer mUpdateLendStatusAndCount(@Param("isbn13") String isbn13);

    // 반납 - 대여상태 false, 대여수 -1
    @Modifying
    @Query("UPDATE Book b SET b.lendStatus = false, b.lendCount = b.lendCount - 1 WHERE b.isbn13 = :isbn13")
    Integer mUpdateLendStatusAndCountReturn(@Param("isbn13") String isbn13);

    // 예약 상태인 책 return
    @Query("select b from Book b where b.isbn13 = :isbn13 and b.reservationStatus = true and b.reservationCount > 0")
    List<Book> mFindBookWithActiveReservation(@Param("isbn13") String isbn13);




    // isbn으로 도서 찾기
    Optional<Book> findByIsbn13(String isbn13);



}

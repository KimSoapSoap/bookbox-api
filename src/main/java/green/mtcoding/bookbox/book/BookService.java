package green.mtcoding.bookbox.book;

import green.mtcoding.bookbox.category.Category;
import green.mtcoding.bookbox.category.CategoryRepository;
import green.mtcoding.bookbox.core.exception.api.ExceptionApi400;
import green.mtcoding.bookbox.core.exception.api.ExceptionApi404;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    //    public BookResponse.BookSearchDTO 검색기록보기(String title, String author, String publisher){
//
//        if(title == null || author == null || publisher == null){
//            List<Book> bookPG = bookRepository.findAll();
//            return new BookResponse.BookSearchDTO(bookPG, "");
//        }
//        List<Book> searchBookList = bookRepository.mFindAll(title, author, publisher);
//    }
    public List<BookResponse.BookListDTO> 메인책목록보기(){
        List<Book> books = bookRepository.mFindAllWithCategory();
        List<BookResponse.BookListDTO> dtos = new ArrayList<>();
        for(Book book : books) {
            BookResponse.BookListDTO dto = new BookResponse.BookListDTO(book);
            dtos.add(dto);
        }
        return dtos;
    }

    public BookResponse.BookSearchDTO 검색기록보기(String title, String author, String publisher){

        if(title == null || author == null || publisher == null){
            List<Book> bookPG = bookRepository.findAll();
            return new BookResponse.BookSearchDTO(bookPG, "");
        }
        List<Book> searchBookList = bookRepository.mFindAll(title, author, publisher);
    }

    public List<BookResponse.CategoryDTO> 책과카테고리보기(){
        List<Category> categories = bookRepository.mFindAllWithCategoryV2();
        List<BookResponse.CategoryDTO> dtos = new ArrayList<>();
        for(Category category : categories) {
            BookResponse.CategoryDTO dto = new BookResponse.CategoryDTO(category);
            dtos.add(dto);
        }
        return dtos;
    }

    public List<BookResponse.clickCategoryDTO> 카테고리별책보기(String id){
        List<Book> bookList = bookRepository.mFindByCategoryId(id);
        List<BookResponse.clickCategoryDTO> dtos = new ArrayList<>();
        for(Book book : bookList) {
            BookResponse.clickCategoryDTO dto = new BookResponse.clickCategoryDTO(book);
            dtos.add(dto);
        }
        return dtos;
    }

    // TODO: AdminController 도서 CRUD 처리를 위한 로직 - 신민재
    // 도서 등록
    public BookResponse.BookDetailDTO 도서등록(BookRequest.SaveDTO dto) {
        // 카테고리 조회
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ExceptionApi400("카테고리를 찾을 수 없습니다."));

        Book book = dto.toEntity(category); // DTO -> Entity 변환
        bookRepository.save(book);
        return new BookResponse.BookDetailDTO(book);
    }

    // 도서 수정
    public BookResponse.BookDetailDTO 도서업데이트(String isbn13, BookRequest.UpdateDTO dto) {
        Book book = bookRepository.findById(isbn13)
                .orElseThrow(() -> new ExceptionApi400("도서를 찾을 수 없습니다."));

        // 카테고리 조회
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ExceptionApi400("카테고리를 찾을 수 없습니다."));

        // 도서 정보 업데이트
        book.update(dto, category);
        bookRepository.save(book);
        return new BookResponse.BookDetailDTO(book);
    }

    // 도서 삭제
    public void deleteBook(String isbn13) {
        bookRepository.deleteById(isbn13);
    }

    // 도서 상세보기
    public BookResponse.BookDetailDTO 도서상세보기(String isbn13) {
        Book book = bookRepository.findById(isbn13)
                .orElseThrow(() -> new ExceptionApi400("도서를 찾을 수 없습니다."));
        return new BookResponse.BookDetailDTO(book);
    }
}

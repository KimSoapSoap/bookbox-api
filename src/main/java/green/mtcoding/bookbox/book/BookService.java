package green.mtcoding.bookbox.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

//    public BookResponse.BookSearchDTO 검색기록보기(String title, String author, String publisher){
//
//        if(title == null || author == null || publisher == null){
//            List<Book> bookPG = bookRepository.findAll();
//            return new BookResponse.BookSearchDTO(bookPG, "");
//        }
//        List<Book> searchBookList = bookRepository.mFindAll(title, author, publisher);
//    }

}

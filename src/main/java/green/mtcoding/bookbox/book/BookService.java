package green.mtcoding.bookbox.book;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

}

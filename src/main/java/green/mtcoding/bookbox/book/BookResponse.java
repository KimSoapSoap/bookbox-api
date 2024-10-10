package green.mtcoding.bookbox.book;

import lombok.Data;

public class BookResponse {

    @Data
    public static class BookSearchDTO {
        private String isbn13;
        private String title;
        private String author;
        private String publisher;
        private String cover;

        public BookSearchDTO(Book book) {
            this.isbn13 = book.getIsbn13();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.publisher = book.getPublisher();
            this.cover = book.getCover();
        }
    }
}

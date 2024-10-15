package green.mtcoding.bookbox.comment;

import green.mtcoding.bookbox.book.Book;
import lombok.Data;

public class CommentRequest {

    @Data
    public static class SaveDTO {
        private String isbn13;
        private String content;

        public Comment toEntity(Book book) {
            return Comment.builder()
                    .content(content)
                    .book(book)
                    .build();
        }
    }
}

package green.mtcoding.bookbox.comment;

import lombok.Data;

public class CommentResponse {

    @Data
    public static class DTO {
        private Long id;
        private String content;
        private String nick;

        public DTO(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.nick = comment.getUser().getNick();
        }
    }
}

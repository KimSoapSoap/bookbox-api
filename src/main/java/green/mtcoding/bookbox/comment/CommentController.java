package green.mtcoding.bookbox.comment;

import green.mtcoding.bookbox.core.util.Resp;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService
            commentService;

    @PostMapping("/api/books/review")
    public ResponseEntity<?> save(@RequestBody CommentRequest.SaveDTO saveDTO){
        CommentResponse.DTO commentDTO = commentService.댓글쓰기(saveDTO);
        return ResponseEntity.ok(Resp.ok(commentDTO));
    }
    @DeleteMapping("/api/books/review/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id, HttpServletRequest request){
        commentService.댓글삭제(id,request);
        return ResponseEntity.ok(Resp.ok(null));
    }
}

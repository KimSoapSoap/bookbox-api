package green.mtcoding.bookbox.comment;


import green.mtcoding.bookbox.book.Book;
import green.mtcoding.bookbox.book.BookRepository;
import green.mtcoding.bookbox.core.exception.api.ExceptionApi403;
import green.mtcoding.bookbox.core.exception.api.ExceptionApi404;
import green.mtcoding.bookbox.core.util.JwtUtil;
import green.mtcoding.bookbox.user.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLOutput;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void 댓글삭제(Long id, HttpServletRequest request) {
        //1. HttpServletRequest에서 Authorization헤더 에서 JWT추출
        String token = JwtUtil.extractToken(request);
        System.out.println("삭제할때 토큰 : " + token);
        Long userId = JwtUtil.extractUserIdFromToken(token);
        System.out.println("토큰의 유저 아이디 : " + userId);

        Comment commentPS = commentRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi404("댓글을 찾을 수 없습니다"));
        System.out.println("받은 유저 아이디 : " + commentPS.getUser().getId());
        if(commentPS.getUser().getId() != userId){
            throw new ExceptionApi403("댓글 삭제 권한이 없습니다");
        }
        System.out.println("삭제 됨?");
        commentRepository.deleteById(id);
        System.out.println("삭제 됐음");
    }

    @Transactional
    public CommentResponse.DTO 댓글쓰기(CommentRequest.SaveDTO saveDTO){

        //게시글 존재 유무 확인
        Book bookPS = bookRepository.findById(saveDTO.getIsbn13())
                .orElseThrow(()-> new ExceptionApi404("게시글을 찾을 수 없습니다."));
        //2. 비영속 댓글 객체 만들기
        Comment comment = saveDTO.toEntity(bookPS);

        //3. 댓글 저장(comment가 영속화된다)
        commentRepository.save(comment);
        return new CommentResponse.DTO(comment);
    }
}

package green.mtcoding.bookbox.lend;

import green.mtcoding.bookbox.core.util.JwtUtil;
import green.mtcoding.bookbox.core.util.Resp;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LendController {

    private final LendService lendService;

    // 대여하기
    @PostMapping("/api/lends")
    public ResponseEntity<?> lendDo(@RequestHeader("Authorization") String token, @RequestBody LendRequest.SaveDTO request){
        String jwtToken = token.replace("Bearer ", "");
        Long userId = JwtUtil.extractUserIdFromToken(jwtToken);

        LendResponse.LendDTO result = lendService.대여하기(userId, request);

        return ResponseEntity.ok(Resp.ok(result));
    }



    // 반납하기


    // 현재 대여중인 도서 목록
    @GetMapping("/api/lends/list")
    public ResponseEntity<?> lendList(@RequestHeader("Authorization") String token){ //request객체로 받는걸로 바꾸기
        // Bearer 제거
        String jwtToken = token.replace("Bearer ", "");
        // JWT에서 userId 추출
        Long userId = JwtUtil.extractUserIdFromToken(jwtToken);

        LendResponse.ListDTO listDTO = lendService.대여중도서목록조회(userId);

        return ResponseEntity.ok(Resp.ok(listDTO));
    }


    // 대여중인 도서 연장
    @PutMapping("/api/lends/extension")
    public ResponseEntity<?> lendExtension(@RequestHeader("Authorization") String token, @RequestBody LendRequest.ExtendDTO request){
        String jwtToken = token.replace("Bearer ", "");
        Long userId = JwtUtil.extractUserIdFromToken(jwtToken);

        LendResponse.ExtensionDTO result = lendService.대여중인도서연장(userId,request);

        return ResponseEntity.ok(Resp.ok(result));
    }


    // 지금까지 대여했던 도서 목록

}

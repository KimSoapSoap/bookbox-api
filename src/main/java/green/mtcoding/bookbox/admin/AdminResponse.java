package green.mtcoding.bookbox.admin;

import lombok.Builder;
import lombok.Data;

public class AdminResponse {

    // 관리자 정보 반환
    @Data
    @Builder
    public static class AdminInfo {
        private Long id;
        private String username;
        private String email;
        private String phone;
        private String role;
    }

    // 로그인 응답
    @Data
    @Builder
    public static class LoginDTO {
        private Long id;
        private String username;
        private String token; // JWT 토큰
    }

}

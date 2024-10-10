package green.mtcoding.bookbox.admin;


import green.mtcoding.bookbox.core.exception.api.ExceptionApi400;
import green.mtcoding.bookbox.core.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminResponse.LoginResponse login(AdminRequest.LoginDTO request) {
        // 로그인 로직
        Admin admin = adminRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword())
                .orElseThrow(() -> new ExceptionApi400("아이디 또는 비밀번호가 틀렸습니다."));

        String token = JwtUtil.createToken(admin.getId()); // JWT 토큰 발급
        return AdminResponse.LoginResponse.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .token(token)
                .build();
    }

    public AdminResponse.AdminInfo getAdminInfo(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ExceptionApi400("관리자를 찾을 수 없습니다."));

        return AdminResponse.AdminInfo.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .phone(admin.getPhone())
                .role(admin.getRole())
                .build();
    }

    // TODO: 추가적인 crud 기능도 여기 포함 (회원 관리, 도서 관리 등)
}

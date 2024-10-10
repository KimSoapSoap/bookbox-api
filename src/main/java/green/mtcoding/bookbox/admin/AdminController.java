package green.mtcoding.bookbox.admin;


import green.mtcoding.bookbox.core.global.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/api/admins/login")
    public CommonResponse<AdminResponse.LoginResponse> login(@RequestBody AdminRequest.LoginDTO request) {
        return CommonResponse.success(adminService.login(request));
    }

    @GetMapping("/api/admins/{id}")
    public CommonResponse<AdminResponse.AdminInfo> getAdminInfo(@PathVariable Long id) {
        return CommonResponse.success(adminService.getAdminInfo(id));
    }

    // TODO: 추후 다른 crud 관련 API 추가 예정 (admin auth 마무리 하고)

}

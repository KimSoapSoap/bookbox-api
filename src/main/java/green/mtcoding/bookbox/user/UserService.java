package green.mtcoding.bookbox.user;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import green.mtcoding.bookbox.core.exception.api.ExceptionApi400;
import green.mtcoding.bookbox.core.exception.api.ExceptionApi401;

import green.mtcoding.bookbox.core.exception.api.ExceptionApi404;
import green.mtcoding.bookbox.core.exception.api.ExceptionApi500;
import green.mtcoding.bookbox.core.util.JwtUtil;
import green.mtcoding.bookbox.core.util.MyFile;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Transactional
    public UserResponse.JoinDTO 회원가입(UserRequest.JoinDTO joinDTO) {

        Optional<User> userOP= userRepository.findByUsername(joinDTO.getUsername());
        if(userOP.isPresent()) {
            throw new ExceptionApi400("이미 존재하는 유저입니다.");
        }

        User userPS = userRepository.save(joinDTO.toEntity());

        userPS.setProfile("nobody.png");

        return new UserResponse.JoinDTO(userPS);
    }

    // 자동 로그인. 토큰을 돌려줄 필요가 없다.
    public UserResponse.AutoLoginDTO 자동로그인(String accessToken) {
        Optional.ofNullable(accessToken).orElseThrow(() -> new ExceptionApi401("토큰을 찾을 수 없습니다."));
        try {
            User user = JwtUtil.verify(accessToken);
            //존재하는 회원인지 확인. id를 꺼내서 존재하는 회원인지 확인
            User userPS = userRepository.findById(user.getId()).orElseThrow(
                    ()-> new ExceptionApi401("유저네임을 찾을 수 없습니다")
            );
            return new UserResponse.AutoLoginDTO(userPS);
        }catch (SignatureVerificationException | JWTDecodeException e1) {
            throw new ExceptionApi401("유효한 토큰이 아닙니다.");
        } catch (TokenExpiredException e2){
            throw new ExceptionApi401(("토큰이 만료되었습니다."));
        }
    }





    public UserResponse.LoginDTO 로그인(UserRequest.LoginDTO loginDTO) {
        // 1. 해당 유저가 있는 조회
        User user = userRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword())
                .orElseThrow(() -> new ExceptionApi401("인증되지 않았습니다"));

        // 2. 조회가 되면, JWT 만들고 응답하기
        String accessToken = JwtUtil.createUserToken(user);

        return new UserResponse.LoginDTO(user, accessToken);
    }

    @Transactional
    public UserResponse.UpdateInfoDTO 회원정보수정(Long userId, UserRequest.UpdateUserDTO updateUserDTO, MultipartFile profile){

        // 유저 id로 해당 유저가 있는지 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new ExceptionApi404("회원 정보가 없습니다."));

        // 프로필 이미지 파일이 선택되지 않았으면 기존 이미지 유지
        if (profile != null && !profile.isEmpty()) {
            String imageFileName = MyFile.파일저장(profile);
            user.setProfile(imageFileName); // 새로운 프로필 이미지 설정
        }

        // 조회된 user 객체에 새로운 값 적용
        if (updateUserDTO.getPassword() != null && !updateUserDTO.getPassword().isEmpty()) {
            user.setPassword(updateUserDTO.getPassword());
        }
        user.setNick(updateUserDTO.getNick());
        user.setPhone(updateUserDTO.getPhone());
        user.setEmail(updateUserDTO.getEmail());
        user.setModifiedAt(Timestamp.valueOf(LocalDateTime.now()));

        // 변경된 user 객체를 저장
        userRepository.save(user);

        return new UserResponse.UpdateInfoDTO(user);
    }


    public boolean 유저네임중복체크 (String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean 닉네임중복체크 (String nick) {
        return userRepository.existsByNick(nick);

    }


    @Transactional
    public UserResponse.UpdateNickDTO 닉네임변경(Long userId, UserRequest.UpdateNickDTO updateNickDTO){

        String nick = updateNickDTO.getNick();

        // 유저 조회
        User userPS = userRepository.findById(userId)
                .orElseThrow(() -> new ExceptionApi404("회원 정보를 찾을 수 없습니다."));

        // 본인의 기존 닉네임과 동일한지 체크
        if (userPS.getNick().equals(nick)) {
            throw new ExceptionApi400("닉네임이 변경되지 않았습니다. 다른 닉네임을 입력해주세요.");
        }

        // 중복체크
        boolean isDuplicateNick  = userRepository.existsByNick(nick);

        if(isDuplicateNick){
            throw new ExceptionApi400("중복된 닉네임입니다.");
        }

        // 변경 ( 수정일도 찍히도록 )
        Integer i = userRepository.mUpdateNick(userId, nick);

        if(i != 1){
            throw new ExceptionApi500("닉네임 변경 중 문제가 발생했습니다.");

        }

        // 영속성 컨텍스트 초기화
        entityManager.clear();

        User userPS2 = userRepository.findById(userId).orElseThrow(() -> new ExceptionApi404("회원 정보 없음"));

        return new UserResponse.UpdateNickDTO(userPS2);


    }
    // 회원 목록 조회(유저의 대여 목록과 예약 목록 함께 return) TODO: 신민재
    public List<UserResponse.UserDTO> getUserList() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponse.UserDTO::new)
                .collect(Collectors.toList());

    }



/*    public User 회원탈퇴(Long id) {
        // 조회
        Optional<User> userOP = userRepository.findById(id);
        // 없으면 예외던지기
        if(userOP.isEmpty() || userOP.get().getId() == null) {
            throw new ExceptionApi404("유저를 찾을 수 없습니다.");
        }

        // ID로 사용자 삭제
        userRepository.deleteById(id);
        // 삭제한 유저 정보 반환
        User user = userOP.get();
        return user;
    }*/
}

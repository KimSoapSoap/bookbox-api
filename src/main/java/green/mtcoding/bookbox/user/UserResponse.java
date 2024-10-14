package green.mtcoding.bookbox.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import green.mtcoding.bookbox.lend.LendResponse;
import green.mtcoding.bookbox.reservation.ReservationResponse;
import lombok.Builder;
import lombok.Data;

import javax.swing.text.View;
import java.sql.Timestamp;

import java.util.List;
import java.util.stream.Collectors;


public class UserResponse {
  
    // 회원가입
    @Data
    public static class JoinDTO {
        private Long id;
        private String username;
        private String nick;
        private String email;
        private String phone;
        private String profile;

        public JoinDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.nick = user.getNick();
            this.email = user.getEmail();
            this.phone = user.getPhone();
            this.profile = user.getProfile();
        }
    }

    // 자동 로그인
    record AutoLoginDTO(Long id, String username) {
        AutoLoginDTO(User user) {
            this(user.getId(), user.getUsername());
        }
    }


    // 로그인
    @Data
    public static class LoginDTO {
        private Long id;
        private String username;
        @JsonIgnore
        private String accessToken;

        @Builder
        public LoginDTO(User user, String accessToken) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.accessToken = accessToken;
        }
    }
    
    // 회원정보 수정 - 없어진 기능
    @Data
    public static class UpdateInfoDTO {
        private Long id;
        private String username;
        private String nick;
        private String password;
        private String email;
        private String phone;
        private String profile;
        private Timestamp modifiedAt;
        private Timestamp createdAt;

        public UpdateInfoDTO(User userPS) {
            this.id = userPS.getId();
            this.username = userPS.getUsername();
            this.nick = userPS.getNick();
            this.password = userPS.getPassword();
            this.email = userPS.getEmail();
            this.phone = userPS.getPhone();
            this.profile = userPS.getProfile();
            this.modifiedAt = userPS.getModifiedAt();
            this.createdAt = userPS.getCreatedAt();
        }
    }
    
    // 유저네임 중복체크
    @Data
    public static class UsernameDupCheckDTO {
        private String username;

        public UsernameDupCheckDTO(String username) {
            this.username = username;
        }
    }

    
    // 닉네임 중복체크
    @Data
    public static class NickDupCheckDTO {
        private String nick;

        public NickDupCheckDTO(String nick) {
            this.nick = nick;
        }
    }

    // 닉네임 수정
    @Data
    public static class UpdateNickDTO {
        private String nick;
        private Long userId;

        public UpdateNickDTO(User userPS) {
            this.userId = userPS.getId();
            this.nick = userPS.getNick();

        }
    }

    public static class LendDTO {
        private String bookTitle;
        private Timestamp lendDate;

        public LendDTO(String bookTitle, Timestamp lendDate) {
            this.bookTitle = bookTitle;
            this.lendDate = lendDate;

        }
    }


    public static class ReservationDTO {
        private String bookTitle;
        private Timestamp reservationDate;

        public ReservationDTO(String bookTitle, Timestamp reservationDate) {
            this.bookTitle = bookTitle;
            this.reservationDate = reservationDate;

        }
    }

    // TODO: 유저의 기본 정보와 필요 데이터 응답을 위해 생성 - 신민재

    // 유저 기본 정보들
    @Data
    public static class UserDTO {
        private Long id;
        private String username;
        private String nick;
        private String email;
        private String phone;

        public UserDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.nick = user.getNick();
            this.email = user.getEmail();
            this.phone = user.getPhone();
        }
    }

    // 특정 유저의 예약 및 대여 목록
    @Data
    public static class UserDetailsDTO {
        private Long id;
        private String username;
        private String nick;
        private List<LendDTO> lends;  // 대여 목록
        private List<ReservationDTO> reservations;  // 예약 목록

        // 예약 및 대여 목록을 포함한 생성자
        public UserDetailsDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.nick = user.getNick();

            // 대여 목록 매핑
            this.lends = user.getLends().stream()
                    .map(lend -> new LendDTO(lend.getBook().getTitle(), lend.getLendDate()))
                    .collect(Collectors.toList());

            // 예약 목록 매핑
            this.reservations = user.getReservations().stream()
                    .map(reservation -> new ReservationDTO(reservation.getBook().getTitle(), reservation.getReservationDate()))
                    .collect(Collectors.toList());


        }
    }

    // 조회
    @Data
    public static class CheckResponse { // 조회
        private Long id;
        private String username;
        private String email;
        private String phone;

        @Builder
        public CheckResponse(Long id, String username, String email, String phone) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.phone = phone;
        }
    }


}

package green.mtcoding.bookbox.user;


import green.mtcoding.bookbox.lend.Lend;
import green.mtcoding.bookbox.reservation.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "user_tb")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;
    @NotBlank
    private String password;

    private String phone;
    private String email;
    @Column
    private Timestamp createdAt;
    @Column
    private Timestamp modifiedAt;

    //이미지 폴더 주소 보관 변수(이름 마음에 드는 걸로 변경 ㄱㄱ싱)
    private String profile;

    @OneToMany(mappedBy = "user")
    private List<Lend> lends = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations = new ArrayList<>();

    // 이메일 수정
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    @Builder
    public User(Long id, String username, String password, String phone, String email, Timestamp createdAt, Timestamp modifiedAt, String profile) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.profile = profile;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

}

package green.mtcoding.bookbox.lend;


import green.mtcoding.bookbox.book.Book;
import green.mtcoding.bookbox.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "lend_tb")
public class Lend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp lendDate;
    @Column(nullable = true)
    private Timestamp returnDate;

    @Column
    private boolean returnStatus;
    @Column
    private boolean extendStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "isbn13") // referencing the String id
    private Book book;

    @PrePersist
    public void prePersist() {
        this.returnStatus = false;
        this.extendStatus = false;
    }

    @Builder
    public Lend(Long id, Timestamp lendDate, Timestamp returnDate, boolean returnStatus, boolean extendStatus, User user, Book book) {
        this.id = id;
        this.lendDate = lendDate;
        this.returnDate = returnDate;
        this.returnStatus = returnStatus;
        this.extendStatus = extendStatus;
        this.user = user;
        this.book = book;
    }
}

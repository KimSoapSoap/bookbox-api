package green.mtcoding.bookbox.lend;

import green.mtcoding.bookbox.book.Book;
import green.mtcoding.bookbox.book.BookRepository;
import green.mtcoding.bookbox.core.exception.api.ExceptionApi400;
import green.mtcoding.bookbox.core.exception.api.ExceptionApi404;
import green.mtcoding.bookbox.core.exception.api.ExceptionApi500;
import green.mtcoding.bookbox.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LendService {

    private final LendRepository lendRepository;
    private final BookRepository bookRepository;

    @Transactional
    public LendResponse.LendDTO 대여하기(Long userId, LendRequest.SaveDTO request){
    // 1. 해당 isbn의 도서가 있는지 체크 ( 유저는 token에서 꺼낸거니까 체크안함 )
        Book bookPS = bookRepository.findById(request.getIsbn13()).orElseThrow(() -> new ExceptionApi404("요청하신 도서가 존재하지 않습니다."));
        // 2. 누군가 lend 했는지 체크
        Boolean b = bookRepository.mCheckLendStatus(request.getIsbn13()).orElseThrow(() -> new ExceptionApi404("요청하신 도서가 존재하지 않습니다."));
        // 2-1. lend = true 상태이면 throw -> 누군가 lend해서 대여불가
        if(b.booleanValue()){
            throw new ExceptionApi400("이미 대여된 도서입니다.");

        }
        // 3. 대여해주기 -> book 대여상태 true, 대여수+1 하고 lend 테이블에도 추가
        Integer i = bookRepository.mUpdateLendStatusAndCount(request.getIsbn13());

        if(1 == 0){
            throw new ExceptionApi500("대여 실패");
        }

        // 4. 유저와 책 객체 만들어서 저장
        User user = new User();
        user.setId(userId);  // userId만 설정

        // 이미 조회한 bookPS 사용
        Lend lendEntity = request.toEntity(user, bookPS);

        // Lend 엔티티 저장
        lendRepository.save(lendEntity);

        // 대여정보 return
        return new LendResponse.LendDTO(lendEntity);

    }


    // book_Tb -> 대여상태 false, 대여수 -1
    // lend_tb -> 반납한 일자 (now), 반납상태 -> true
    @Transactional
    public LendResponse.ReturnDTO 직접반납하기(Long userId, LendRequest.ReturnDTO request) {

        // 대여상태인지 확인
        Boolean b = bookRepository.mCheckLendStatus(request.getIsbn13()).orElseThrow(() -> new ExceptionApi404("요청하신 도서가 존재하지 않습니다."));

        if(!b){
            throw new ExceptionApi404("대여중인 도서가 아닙니다.");
        }

        // 2. booktb 대여 상태 바꾸기
        int updateCount = bookRepository.mUpdateLendStatusAndCountReturn(request.getIsbn13());

        // 업데이트가 성공했는지 확인 (1이 아니면 실패)
        if (updateCount != 1) {
            throw new ExceptionApi500("도서 반납 처리 중 문제가 발생했습니다.");
        }

        // 3.lend_tb 대여 상태 바꾸기
        int returnStatus = lendRepository.mReturnLend(userId, request.getIsbn13());

        // 업데이트 성공했는지 확인 (1이 아니면 실패)
        if (returnStatus != 1) {
            throw new ExceptionApi500("도서 반납 처리 중 문제가 발생했습니다.");
        }
        
        // 반납정보 return
        Lend lendPS = lendRepository.mFindLend(userId, request.getIsbn13());

        return new LendResponse.ReturnDTO(lendPS);

    }

    // 자동으로 반납시키기 ( 반납일 12시에 자동으로 반납됨 )
    // 스케줄링 설정 ?




    public LendResponse.ListDTO 대여중도서목록조회(Long userId){

        List<Object[]> objectsPS = lendRepository.mFindBooksByUserId(userId);

        return new LendResponse.ListDTO(objectsPS); // 도서 cover, title, isbn13 -> list로!
    }

    @Transactional
    public LendResponse.ExtensionDTO 대여중인도서연장(Long userId, LendRequest.ExtendDTO request){
        // 연장 상태가 false 이면 true로 바꾸고, 반납한 일자 +7일
        // 연장 상태가 true 이면 연장불가
        // 1. 조회
        Boolean resultPS = lendRepository.mCheckExtendStatus(userId, request.getIsbn13()).orElseThrow(() -> new ExceptionApi404("대여정보가 없습니다."));

        if(resultPS){
            throw new ExceptionApi400("이미 연장한 도서입니다. 더이상 연장이 불가합니다.");
        }

        // 2. 연장해주기
        lendRepository.mExtendLend(userId, request.getIsbn13());

        // 3. 연장 내역 return
        Lend lendPS = lendRepository.mFindLend(userId, request.getIsbn13());

        return new LendResponse.ExtensionDTO(lendPS);

    }

    // userId로 조회
    // 중복 제거
    public void 지금까지대여한도서들목록(Long userId){


    }
}
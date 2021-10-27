package org.zerock.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service // 스프링에서 빈으로 처리되도록 어노테이션 추가
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService{

    private final GuestbookRepository repository; // 반드시 final로 선언

    @Override
    public Long register(GuestbookDTO dto){
        log.info("DTO ----------------");
        log.info(dto);
        
        // dtoToEntity()를 활용해 파라미터로 전달되는 GuestbookDTO 변환
        Guestbook entity = dtoToEntity(dto);
        log.info(entity);

        // resister()의 내부에서는 save()를 통해 저장 --> 해당 엔티티가 가지는 gno값을 반환
        repository.save(entity);
        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO){
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        BooleanBuilder booleanBuilder = getSearch(requestDTO); // 검색 조건 처리
        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable); // Querydsl 사용
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result,fn);
    }
    
    @Override // 인터페이스에 정의된 read() 기능 구현
    public GuestbookDTO read(Long gno){
        Optional<Guestbook> result = repository.findById(gno);
        // 엔티티 객체를 가지고 왔다면 entityToDto()를 이용해 엔티티 객체를 DTO로 변환해 반환
        return result.isPresent()? entityToDto(result.get()): null;
    }

    @Override // 삭제 기능
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    @Override // 수정 기능
    public void modify(GuestbookDTO dto) {
        Optional<Guestbook> result = repository.findById(dto.getGno());

        if(result.isPresent()){
            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity);
        }
    }

    // 검색 기능을 위해 추가
    private BooleanBuilder getSearch(PageRequestDTO requestDTO){ // Querydsl 처리
        String type = requestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = requestDTO.getKeyword();
        BooleanExpression expression = qGuestbook.gno.gt(0L); // gno > 0 조건만 생성
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0){ // 검색 조건이 없는 경우
            return booleanBuilder;
        }

        // 검색 조건 작성
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")){
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }

        if(type.contains("c")){
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }

        if(type.contains("w")){
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        // 모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }
}

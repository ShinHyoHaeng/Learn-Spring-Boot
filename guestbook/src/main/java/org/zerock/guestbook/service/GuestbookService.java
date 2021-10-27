package org.zerock.guestbook.service;

import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;

public interface GuestbookService {

    // GuestbookDTO를 이용해서 새로운 '방명록'을 등록하는 시나리오 처리
    Long register(GuestbookDTO dto);

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);
    
    /*
    *   엔티티 타입의 객체로 변환 작업 필요(* 서비스 계층에서는 파라미터를 DTO 타입으로 받기 때문에 이를 JPA로 처리하기 위해서는 해당 작업 필수)
    *    
    *   default 메서드: 인터페이스의 실제 내용을 가지는 코드를 'default'라는 키워드로 생성 가능(Java 8 버전 이후)
    *   이를 이용하면 기존에 추상 클래스를 통해 전달해야 하는 실제 코드를 인터페이스에 선언 가능
    *   기존의 '인터페이스 -> 추상 클래스 -> 구현 클래스'의 과정에서 추상 클래스 생략 가능
    */
    // default 기능을 활용해 구현클래스에서 동작할 수 있는 dtoToEntity() 구성
    default Guestbook dtoToEntity(GuestbookDTO dto){
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    // default 기능을 활용해 구현클래스에서 동작할 수 있는 entityToDto() 구성
    default GuestbookDTO entityToDto(Guestbook entity){
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }

    // read() 메서드 추가
    // 파라미터: Long 타입의 gno
    // 리턴 타입: GuestbookDTO
    GuestbookDTO read(Long gno);
    
    // 수정과 삭제 기능 메서드 추가
    void remove(Long gno);
    void modify(GuestbookDTO dto);
}

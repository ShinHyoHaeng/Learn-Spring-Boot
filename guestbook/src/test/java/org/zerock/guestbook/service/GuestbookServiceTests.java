package org.zerock.guestbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

@SpringBootTest
public class GuestbookServiceTests {

    @Autowired
    private GuestbookService service;

    @Test // 서비스 계층의 테스트(새로운 번호의 데이터 추가)
    public void testRegister(){
        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample Title")
                .content("Sample Content")
                .writer("user0")
                .build();
        System.out.println(service.register(guestbookDTO));
    }

    @Test // 목록 처리 테스트(수정)
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV: "+resultDTO.isPrev());
        System.out.println("NEXT: "+resultDTO.isNext());
        System.out.println("TOTAL: "+resultDTO.getTotalPage());

        System.out.println("-----------------------------");
        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()){
            System.out.println(guestbookDTO);
        }

        System.out.println("=============================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }

    @Test
    public void testSearch(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc")
                .keyword("테스트")
                .build();

        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);
        System.out.println("PREV : "+resultDTO.isPrev());
        System.out.println("NEXT : "+resultDTO.isNext());
        System.out.println("TOTAL : "+resultDTO.getTotalPage());

        System.out.println("--------------------------------------");
        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()){
            System.out.println(guestbookDTO);
        }

        System.out.println("=======================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
    }

}

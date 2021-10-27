package org.zerock.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
    // 화면에 전달되는 page, size 파라미터 수집 역할
    private int page;
    private int size;
    private String type; // 검색 기능을 위해 추가
    private String keyword; // 검색 기능을 위해 추가

    public PageRequestDTO(){ // 기본값 지정
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page -1, size, sort);
    }

}

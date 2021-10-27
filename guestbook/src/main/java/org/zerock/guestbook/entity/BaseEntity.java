package org.zerock.guestbook.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass // 해당 어노테이션이 적용된 클래스는 테이블로 생성 X --> 실제 테이블은 BaseEntity 클래스를 상속한 엔티티 클래스로 DB 테이블이 생성
@EntityListeners(value = {AuditingEntityListener.class}) // AuditingEntityListener: JPA 내부에서 엔티티 객체가 생성/변경되는 것을 감지하는 역할
@Getter
abstract class BaseEntity {

    // 객체의 등록 시간 담당
    @CreatedDate // JPA에서 엔티티의 생성 시간을 처리
    @Column(name = "regdate", updatable = false) // updatable = false: 해당 엔티티 객체를 데이터베이스에 반영할 때 regdate 칼럼값 변경 X
    private LocalDateTime regDate;

    // 객체의 최종 수정 시간 담당
    @LastModifiedDate // 최종 수정 시간을 자동으로 처리하는 용도로 사용
    @Column(name="moddate")
    private LocalDateTime modDate;
}

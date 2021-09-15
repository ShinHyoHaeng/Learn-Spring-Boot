package org.zerock.ex2.entity;

import lombok.*; // lombok: 자동으로 코드를 만들어주는 역할
import javax.persistence.*;

@Entity // 해당 클래스가 엔티티를 위한 클래스 --> 해당 클래스의 인스턴스들이 JPA로 관리되는 엔티티 객체라는 것을 의미
@Table(name= "tbl_memo") // 'tbl_memo' 테이블 생성
@ToString
@Getter // Getter 메서드 자동 생성
@Builder // 객체 생성
// @AllArgsConstructor, @NoArgsConstructor를 사용하지 않으면 컴파일 오류 발생
@AllArgsConstructor
@NoArgsConstructor

public class Memo {
    @Id // PRIMARY KEY 사용 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK를 자동으로 생성하고자 할 때 사용(키 생성 전략). 'auto-increment'를 기본으로 사용
    private Long mno;

    @Column(length = 200, nullable = false) // 추가적인 필드가 필요할 때 활용
    private String memoText;
}

package org.zerock.guestbook.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class GuestbookRepositoryTests {

    @Autowired
    private GuestbookRepository guestbookRepository;

    @Test // 300개의 테스트용 데이터 생성
    public void insertDummies(){
        IntStream.rangeClosed(1,300).forEach(i -> {

            Guestbook guestbook = Guestbook.builder()
                    .title("Title "+i)
                    .content("Content "+i)
                    .writer("user "+(i%10))
                    .build();
            System.out.println(guestbookRepository.save(guestbook));
        });
    }

    @Test // 특정한 엔티티를 수정한 후 save()했을 경우 최종 수정 시간이 제대로 반영되는지 테스트
    public void updateTest(){
        Optional<Guestbook> result = guestbookRepository.findById(300L); // 존재하는 번호로 테스트

        if(result.isPresent()){
            Guestbook guestbook = result.get();

            guestbook.changeTitle("Changed title");
            guestbook.changeContent("Changed content");

            guestbookRepository.save(guestbook);
        }
    }

    /*
    *   테스트 결과(console)
        Hibernate:
            select
                guestbook0_.gno as gno1_0_0_,
                guestbook0_.moddate as moddate2_0_0_,
                guestbook0_.regdate as regdate3_0_0_,
                guestbook0_.content as content4_0_0_,
                guestbook0_.title as title5_0_0_,
                guestbook0_.writer as writer6_0_0_
            from
                guestbook guestbook0_
            where
                guestbook0_.gno=?
        Hibernate:
            select
                guestbook0_.gno as gno1_0_0_,
                guestbook0_.moddate as moddate2_0_0_,
                guestbook0_.regdate as regdate3_0_0_,
                guestbook0_.content as content4_0_0_,
                guestbook0_.title as title5_0_0_,
                guestbook0_.writer as writer6_0_0_
            from
                guestbook guestbook0_
            where
                guestbook0_.gno=?
    */

    @Test // 단일 항목 검색 테스트(조건: 제목에 '1'이라는 글자가 있는 엔티티 검색)
    public void testQuery1(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").descending());

        // 1. Q도메인 클래스 선언(동적 처리): 엔티티 클래스에 선언된 title, content같은 필드들을 변수로 활용 가능
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "1";

        // 2. BooleanBuilder: where문에 들어가는 조건들을 넣어주는 컨테이너 역할
        BooleanBuilder builder = new BooleanBuilder();

        // 3. 원하는 조건은 필드 값과 함께 결합해 생성
        // BooleanBuilder 안에 들어가는 값은 com.querydsl.core.types.Predicate 타입(Java에 있는 Predicate 타입 X)
        BooleanExpression expression = qGuestbook.title.contains(keyword);

        // 4. 만들어진 조건을 where문에 and이나 or 같은 키워드와 결합
        builder.and(expression);

        // 5. BooleanBuilder는 GuestbookRepository에 추가된 QuerydslPredicateExcutor 인터페이스의 findAll() 사용 가능
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }
    /*
    *   테스트 결과(console)
        Hibernate:
            select
                guestbook0_.gno as gno1_0_,
                guestbook0_.moddate as moddate2_0_,
                guestbook0_.regdate as regdate3_0_,
                guestbook0_.content as content4_0_,
                guestbook0_.title as title5_0_,
                guestbook0_.writer as writer6_0_
            from
                guestbook guestbook0_
            where
                guestbook0_.title like ? escape '!'
            order by
                guestbook0_.gno desc limit ?
        Hibernate:
            select
                count(guestbook0_.gno) as col_0_0_
            from
                guestbook guestbook0_
            where
                guestbook0_.title like ? escape '!'

        Guestbook(gno=291, title=Title 291, content=Content 291, writer=user 1)
        Guestbook(gno=281, title=Title 281, content=Content 281, writer=user 1)
        Guestbook(gno=271, title=Title 271, content=Content 271, writer=user 1)
        Guestbook(gno=261, title=Title 261, content=Content 261, writer=user 1)
        Guestbook(gno=251, title=Title 251, content=Content 251, writer=user 1)
        Guestbook(gno=241, title=Title 241, content=Content 241, writer=user 1)
        Guestbook(gno=231, title=Title 231, content=Content 231, writer=user 1)
        Guestbook(gno=221, title=Title 221, content=Content 221, writer=user 1)
        Guestbook(gno=219, title=Title 219, content=Content 219, writer=user 9)
        Guestbook(gno=218, title=Title 218, content=Content 218, writer=user 8)
    */

    @Test // 다중 항목 검색 테스트(조건: 제목 혹은 내용에 특정한 키워드가 있고 gno가 0보다 크다)
    public void testQuery2(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("gno").descending());
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = "1";
        BooleanBuilder  builder = new BooleanBuilder();
        BooleanExpression exTitle = qGuestbook.title.contains(keyword);
        BooleanExpression exContent = qGuestbook.content.contains(keyword);

        // 1. BooleanExpression(exTitle, exContent) 결합
        BooleanExpression exAll = exTitle.or(exContent);

        // 2. 결합한 BooleanExpression을 BooleanBuilder에 추가
        builder.and(exAll);

        // 3. 'gno'가 0보다 크다라는 조건 추가
        builder.and(qGuestbook.gno.gt(0L));

        Page<Guestbook> result = guestbookRepository.findAll(builder,pageable);

        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

    /*
    *   테스트 결과
        Hibernate:
            select
                guestbook0_.gno as gno1_0_,
                guestbook0_.moddate as moddate2_0_,
                guestbook0_.regdate as regdate3_0_,
                guestbook0_.content as content4_0_,
                guestbook0_.title as title5_0_,
                guestbook0_.writer as writer6_0_
            from
                guestbook guestbook0_
            where
                (
                    guestbook0_.title like ? escape '!'
                    or guestbook0_.content like ? escape '!'
                )
                and guestbook0_.gno>?
            order by
                guestbook0_.gno desc limit ?
        Hibernate:
            select
                count(guestbook0_.gno) as col_0_0_
            from
                guestbook guestbook0_
            where
                (
                    guestbook0_.title like ? escape '!'
                    or guestbook0_.content like ? escape '!'
                )
                and guestbook0_.gno>?
        Guestbook(gno=291, title=Title 291, content=Content 291, writer=user 1)
        Guestbook(gno=281, title=Title 281, content=Content 281, writer=user 1)
        Guestbook(gno=271, title=Title 271, content=Content 271, writer=user 1)
        Guestbook(gno=261, title=Title 261, content=Content 261, writer=user 1)
        Guestbook(gno=251, title=Title 251, content=Content 251, writer=user 1)
        Guestbook(gno=241, title=Title 241, content=Content 241, writer=user 1)
        Guestbook(gno=231, title=Title 231, content=Content 231, writer=user 1)
        Guestbook(gno=221, title=Title 221, content=Content 221, writer=user 1)
        Guestbook(gno=219, title=Title 219, content=Content 219, writer=user 9)
        Guestbook(gno=218, title=Title 218, content=Content 218, writer=user 8)
    */


    @Test
    public void testQuery() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("gno").ascending());
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression exTitle = qGuestbook.title.contains(keyword); // contains == 쿼리문의 like
        BooleanExpression exContent = qGuestbook.content.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent);
        builder.and(exAll);
        builder.and(qGuestbook.gno.gt(0L));
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
        result.stream().forEach(guestbook -> {
            System.out.println(guestbook);
        });
    }

}

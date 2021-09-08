package org.zerock.ex2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Commit;
import org.zerock.ex2.entity.Memo;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MemoRepositoryTests {
    @Autowired
    MemoRepository memoRepository;

    @Test // MemoRepository 인터페이스 타입의 실제 객체 확인
    public void testClass(){
        System.out.println("1 =======================");
        System.out.println(memoRepository.count());
        System.out.println("2 =======================");

        System.out.println(memoRepository.getClass().getName());
    }

    @Test // 'tbl_memo'에 row 100개 삽입
    public void testInsert(){
        for(int i=1; i<=100; i++){
            Memo memo = Memo.builder().memoText("Sample..."+i).build();
            memoRepository.save(memo);
        }
    }

    @Test // 'tbl_memo'에서 100번 row 찾아서 출력하기
    public void testSelect(){
        Long mno = 100L;

        // = select * from tbl_memo where mno=100;
        Optional<Memo> result = memoRepository.findById(mno);
        System.out.println("1 =======================");
        Memo memo = result.get();
        System.out.println("번호: " + memo.getMno());
        System.out.println("내용: " + memo.getMemoText());
        System.out.println("2 =======================");
    }

    @Test // 'tbl_memo'에서 100번 row 수정
    public void testUpdate(){
        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
        System.out.println("1 =======================");
        System.out.println(memoRepository.save(memo));
        System.out.println("2 =======================");
    }

    @Test // 'tbl_memo'에서 100번 row 삭제
    public void testDelete(){
        memoRepository.deleteById(100L);
    }

    @Test // 페이징 처리
    public void testPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println("1 =======================");
        System.out.println(result);
        System.out.println("Total Page : " + result.getTotalPages());
        System.out.println("Total Count : " + result.getTotalElements());
        System.out.println("Page Number : " + result.getNumber());
        System.out.println("Page Size : " + result.getSize());
        System.out.println("has next page? : " + result.hasNext());
        System.out.println("first page? : " + result.isFirst());
        System.out.println("2 =======================");
        for(Memo memo:result.getContent()){
            System.out.println("번호 "+ memo.getMno()+"번 : "+memo.getMemoText());
        }
        System.out.println("3 =======================");
    }

    @Test // 페이지 정렬(내림차순/오름차순)
    public void testSort(){
        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2);
        Pageable pageable = PageRequest.of(0,10,sortAll);
        Page<Memo> result = memoRepository.findAll(pageable);
        result.get().forEach(memo -> {
            System.out.println("번호 "+ memo.getMno()+"번 : "+memo.getMemoText());
        });
    }

    @Test
    public void testQueryMethods(){
        // = select * from tbl_memo where mno between 70 and 80;
        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L,80L);
        System.out.println("1 =======================");
        for(Memo memo : list){
            System.out.println("번호 "+ memo.getMno()+"번 : "+memo.getMemoText());
        }
        System.out.println("2 =======================");
    }

    @Test
    public void testQueryMethodWithPageable(){
        // =  select * from tbl_memo where mno between 10 and 50 order by mno desc limit 10;
        Pageable pageable = PageRequest.of(0,10,Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L,50L, pageable);
        System.out.println("1 =======================");
        result.get().forEach(memo -> System.out.println(memo));
        System.out.println("2 =======================");
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods(){
        // = delete from tbl_memo where mno<10;
        memoRepository.deleteMemoByMnoLessThan(10L);
    }

    @Test
    public void testQuery(){
        List<Memo> result = memoRepository.getListDesc();
        System.out.println("1 =======================");
        for(Memo memo : result){
            System.out.println("번호 "+ memo.getMno()+"번 : "+memo.getMemoText());
        }
        System.out.println("2 =======================");
    }

    @Test
    public void testQueryUpdate(){
        int result = memoRepository.updateMemoText(10L,"update Query test");
        System.out.println("1 =======================");
        System.out.println(result);
        System.out.println("2 =======================");
    }

    @Test
    public void testQueryDelete(){
        int result = memoRepository.deleteMemo(80L);
        System.out.println("1 =======================");
        System.out.println(result);
        System.out.println("2 =======================");
    }
}

package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test // 사용자가 하나의 게시글을 작성 -> 100개의 dummy data 생성
    public void insertBoard(){
        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder().email("user"+i+"@aaa.com").build();
            Board board = Board.builder()
                    .title("Title "+i)
                    .content("Content "+i)
                    .writer(member)
                    .build();
            boardRepository.save(board);
        });
    }

//    @Test
//    public void testRead1(){
//        Optional<Board> result = boardRepository.findById(100L); // 데이터베이스에 존재하는 번호
//        Board board = result.get();
//        System.out.println("-------------------------");
//        System.out.println(board);
//        System.out.println(board.getWriter());
//        System.out.println("=========================");
//    }


    @Transactional
    @Test
    public void testRead1(){
        Optional<Board> result = boardRepository.findById(100L);
        Board board = result.get();
        System.out.println("-------------------------");
        System.out.println(board);
        System.out.println(board.getWriter());
        System.out.println("=========================");
    }

    @Test
    public void testReadWithWriter() {
        Object result = boardRepository.getBoardWithWriter(100L);
        Object[] arr = (Object[])result;
        System.out.println("------------------------------");
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void testGetBoardWithReply(){
        List<Object[]> result = boardRepository.getBoardWithReply(100L);
        for(Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    public void testWidthReplyCount(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);
        result.get().forEach(row -> {
            Object[] arr = (Object[])row;
            System.out.println(Arrays.toString(arr));
        });
    }

    @Test
    public void testRead3(){
        Object result = boardRepository.getBoardByBno(100L);
        Object[] arr = (Object[])result;
        System.out.println(Arrays.toString(arr));
    }
}
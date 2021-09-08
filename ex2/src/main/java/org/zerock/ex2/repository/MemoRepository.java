package org.zerock.ex2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.ex2.entity.Memo;

import javax.transaction.Transactional;
import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long>{
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);
    void deleteMemoByMnoLessThan(Long num);

    @Query("select m from Memo m order by m.mno desc")
    List<Memo> getListDesc();

    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText=:memoText where m.mno=:mno")
    int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);

    // 파라미터로 번호를 가져온 뒤 그 번호보다 큰 값을 삭제(delete)
    @Transactional
    @Modifying
    @Query("delete Memo m where m.mno >= :mno")
    int deleteMemo(@Param("mno") Long mno);



}

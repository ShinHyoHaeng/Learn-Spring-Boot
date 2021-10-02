package org.zerock.guestbook.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.zerock.guestbook.entity.Guestbook;

// JpaRepository를 상속받는 인터페이스로 구성
// Querydsl을 이용 --> QuerydslPredicateExecutor라는 인터페이스를 추가로 상속
public interface GuestbookRepository extends JpaRepository<Guestbook, Long>, QuerydslPredicateExecutor<Guestbook> {
}

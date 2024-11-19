package com.example.setty.repository;

import com.example.setty.entity.Member;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Repository //레포지토리 = 데이터 접근 계층. 자동으로 빈으로 관리하도록 함.
@RequiredArgsConstructor //Lombok 어노테이션. final 필드에 대해 생성자를 자동으로 생성
public class MemberRepository {

    @PersistenceContext //EntityManager를 주입하는 역할. / JPA의 인터페이스. 데이터베이스와의 상호작용을 관리.
    private final EntityManager em;

    public void create(Member member) {
        em.persist(member); //member 객체를 데이터베이스의 새로운 행으로 추가.
    }


    public Optional<Member> findByEmail(String email) {
        //특정 타입(Member)을 반환하는 쿼리 생성
        //em.createQuery()-> JPQL 쿼리 작성
        // "select ~" -> JPQL 쿼리. Member 엔티티 대상으로 em
        TypedQuery<Member> typedQuery = em.createQuery("select m from Member m where m.email = :email", Member.class);
        typedQuery.setParameter("email", email); //:email 파라미터를 실제 email값으로 설정.
        try {
            Member member = (Member) typedQuery.getSingleResult();             //쿼리 실행하고, 결과가 하나의 레코드라면 그 레코드 반환. 결과 없으면 NoResultException
            return Optional.ofNullable(member); //null 안전성 보장(혹시라도 모를!) 그리고 일관성 보장
        } catch (NoResultException e) {
            return Optional.empty(); // 결과가 없을 경우 빈 Optional 객체 반환 -> 호출자가 null 처리할 필요 X.
        }

    }

}

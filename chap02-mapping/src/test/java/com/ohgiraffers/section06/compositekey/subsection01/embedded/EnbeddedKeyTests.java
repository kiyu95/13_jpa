package com.ohgiraffers.section06.compositekey.subsection01.embedded;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class EnbeddedKeyTests {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    /*
    * jpa에서 복합 키를 매핑하는 방법에는 두 가지가 있다.
    * 첫 번째 @EmbeddedId 어노테이션을 사용하는 방법이다.
    * 이 방법은 복합 키를 구성하는 필드들을 하나의 클래스로 묶은 뒤, 해당 클래스를 @EmbeddedId 어노테이션을 사용하여 매핑하는 것이다.
    * 이 방법은 복합 키의 일부 필드만을 매핑할 수도 있기 때문에, 필드 수가 많은 경우에는 유연한 매핑이 가능하다는 장점이 있다.
    * 두 번째는 @IdClass 어노테이션을 사용하는 방법이다.
    * 이 방법은 복합 키를 구성하는 필드들을 별도의 클래스로 분리한 뒤, 해당 클래스를 @IdClass 어노테이션의 값으로 지정해주는 것이다.
    * 이 방법은 복합 키를 구성하는 모든 필드를 한 번에 매핑할 수 있으며, 별도의 매핑 클래스를 사용하지 않기 때문에 코드가 간결하다는 장점이 있다.
    *
    * 복합 키의 매핑에서는 복합 키 클래스에 Equals와 hashCode 메소드를 구현해야 한다는 점에 주의해야 한다.
    * 이는 jpa에서 엔티티 객체의 동일성을 판단하기 위해 필요하다.
    * 또한, @GeneratedValue 어노테이션을 사용하여 복합 키를 자동으로 생성하는 것은 불가능하다는 점에 주의해야 한다.
    *
    * 두 방식 모두 복합키 클래스는 영속성 컨텍스트가 관리하지 않는다는 특징이 있으며, 큰 기능적 저하도 존재하지 않는다.
    * 다만 EmbeddedId가 조금 더 객체 지향다운 방법이고, @IdClass는 관계형 데이터베이스에 가까운 방법이다.
    * */

    @Test
    public void 임베디드_아이디_사용한_복합키_테이블_매핑_테스트() {

        // given
        Member member = new Member();
        member.setMemberPK(new MemberPK(1, "user01"));
        member.setPhone("010-1234-5678");
        member.setAddress("서울시 종로구");

        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(member);
        entityTransaction.commit();

        // then
        Member foundMember = entityManager.find(Member.class, member.getMemberPK());
        Assertions.assertEquals(member.getMemberPK(), foundMember.getMemberPK());
    }


}

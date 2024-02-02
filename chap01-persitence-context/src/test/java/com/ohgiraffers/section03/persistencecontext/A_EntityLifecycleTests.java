package com.ohgiraffers.section03.persistencecontext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class A_EntityLifecycleTests {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory(){
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager(){
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory(){
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager(){
        entityManager.close();
    }

    /*
    * 영속성 컨텍스트는 엔티티 매니저가 엔티티 객체를 저장하는 공간으로 엔티티 객체를 보관하고 관리한다.
    * 엔티티 매니저가 생성 될 때 하나의 영속성 컨텍스트가 만들어 진다.
    *
    * 엔티티의 생명 주기
    * 비영속, 영속, 준영속, 삭제 상태
    * */
    @Test
    public void 비영속_테스트(){

        // given
        Menu foundMenu = entityManager.find(Menu.class, 11);

        // 객체만 생성하면 영속성 컨텍스트나 데이터베이스와 관련 없는 비영속 상태이다.
        Menu newMenu = new Menu();
        newMenu.setMenuCode(foundMenu.getMenuCode());
        newMenu.setMenuName(foundMenu.getMenuName());
        newMenu.setMenuPrice(foundMenu.getMenuPrice());
        newMenu.setCategoryCode(foundMenu.getCategoryCode());
        newMenu.setOrderableStatus(foundMenu.getOrderableStatus());

        Menu copyMenu = foundMenu;

        // when
        boolean isTrue = (foundMenu == newMenu); // 값은 같지만 주소값이 다르므로 false
        boolean result = (foundMenu == copyMenu); // 주소값이 같으므로 true

        // then
        Assertions.assertFalse(isTrue);
        Assertions.assertTrue(result);
    }

    @Test
    public void 영속성_연속_조회_테스트(){

        /*
        * 엔티티 매니저가 영속성 컨텍스트에 엔티티 객체를 지정(parsist)하면 영속성 컨텍스트가 엔티티 객체를 관리하게 되고
        * 이를 영속 상태라고 한다. Find(), jpql을 사용한 조회도 영속 상태가 된다.
        * */
        // given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 11);

        // when
        boolean isTrue = (foundMenu1 == foundMenu2);

        // then
        Assertions.assertTrue(isTrue);
    }

    @Test
    public void 영속성_객체_추가_테스트(){

        // Menu Entity의 @GeneratedValue(strategy=GenerationType.IDENTITY) 설정을 잠시 주석하고 테스트 수행

        // given
        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(500);
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(10000);
        menuToRegist.setCategoryCode(1);
        menuToRegist.setOrderableStatus("Y");

        // when
        entityManager.persist(menuToRegist); // 영속성 컨텍스트에 집어넣음
        Menu foundMenu = entityManager.find(Menu.class, 500);
        boolean isTrue = (menuToRegist == foundMenu);

        Assertions.assertTrue(isTrue);
    }

    @Test
    public void 준영속성_detach_테스트() {

        // given
        Menu foundMenu = entityManager.find(Menu.class, 11);
        Menu foundMenu1 = entityManager.find(Menu.class, 12);

        /*
        * 영속성 컨텍스트가 관리하던 엔티티 객체를 관리하지 않는 상태가 된다면 준영속 상태가 된다
        * 그 중 Detach는 특정 엔티티만 준영속 상태로 만든다.
        * */
        // when
        entityManager.detach(foundMenu1);

        foundMenu.setMenuPrice(5000);
        foundMenu1.setMenuPrice(5000);

        Assertions.assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());
//        entityManager.merge(foundMenu1); // 다시 영속화
        Assertions.assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());

    }

    @Test
    void 준영속성_clear_테스트(){

        // given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        // when
        entityManager.clear();

        foundMenu2.setMenuPrice(5000);
        foundMenu1.setMenuPrice(5000);

        Assertions.assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());
        Assertions.assertEquals(5000, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    @Test
    void close_테스트(){

        // given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 12);

        // when
        entityManager.close();

        foundMenu2.setMenuPrice(5000);
        foundMenu1.setMenuPrice(5000);

        // then
        // 영속성 컨텍스트를 닫았기 때문에 다시 만들기 전에는 사용할 수 없다.
        Assertions.assertEquals(5000, entityManager.find(Menu.class, 11).getMenuPrice());
        Assertions.assertEquals(5000, entityManager.find(Menu.class, 13).getMenuPrice());
    }

    @Test
    public void 삭제_remove_테스트(){

        /*
        * remove : 엔티티를 영속성 컨텍스트 및 데이터베이스에서 삭제한다.
        * 단, 트랜젝션을 제어하지 않으면 영구 반영되지는 않는다.
        * 트랜젝션을 커밋하는 순간 영속성 컨텍스트에서 관리하는 엔티티 객체가 데이터베이스에 반영되게 한다(이를 flush라고 한다)
        * Flush : 영속성 컨텍스트의 변경 내용을 데이터베이스에 동기화하는 작업(등록, 수전, 삭제한 엔티티를 데이터베이스에 반영)
        * */

        // given
        Menu foundMenu = entityManager.find(Menu.class, 2);

        // when
        entityManager.remove(foundMenu);
        Menu refoundMenu = entityManager.find(Menu.class, 2);

        Assertions.assertEquals(2, foundMenu.getMenuCode());
        Assertions.assertEquals(null, refoundMenu);
    }

    /*
    * 병합(merge) : 파라미터로 넘어온 준영속 엔티티 객체의 식별 값으로 1차 캐시에서 엔티티 객체를 조회한다.
    * 만약 1차 캐시에 엔티티가 없으면 데이터베이스에서 엔티티를 조회하고 1차 캐시에 저장한다.
    * 조회한 영속 엔티티 객체에 준영속 상태의 엔티티 객체의 값을 병합한 뒤 영속 엔티티 객체를 반환한다.
    * 혹은 조회할 수 없는 데이터의 경우 새로 생성해서 병합한다.(save or update)
    * */
    @Test
    void 병합_merge_수정_테스트(){

        // given
        Menu menuToDetach = entityManager.find(Menu.class, 3);
        entityManager.detach(menuToDetach); // 준영속 상태로 변경

        // when
        menuToDetach.setMenuName("수박죽"); // 준영속 상태의 name값을 변경
        Menu refoundMenu = entityManager.find(Menu.class, 3); // 다시 PK3 의 데이터를 가져옴

        // 준영속 엔티티와 영속 엔티티의 해쉬코드는 다른 상태이다.
        System.out.println(menuToDetach.hashCode()); // 21881683
        System.out.println(refoundMenu.hashCode()); // 32205794

        entityManager.merge(menuToDetach); // 준영속 상태의 값으로 병합시킴

        // then
        Menu mergeMenu = entityManager.find(Menu.class, 3);
        Assertions.assertEquals("수박죽", mergeMenu.getMenuName());
    }

    @Test
    void 병합_merge_삽입_테스트(){

        // given
        Menu menuToDetach = entityManager.find(Menu.class, 3); // 3 조회
        entityManager.detach(menuToDetach); // 3 준영속

        // when
        menuToDetach.setMenuCode(999); // 준영속 상태의 값 변경
        menuToDetach.setMenuName("수박죽"); // name 변경

        entityManager.merge(menuToDetach); // 999를 조회하고 999에 준영속 상태의 값을 병합시킴. 여기에는 999가 없으므로 병합되지 않음

        // then
        Menu mergeMenu = entityManager.find(Menu.class, 999);
        Assertions.assertEquals("수박죽", mergeMenu.getMenuName());
    }
}

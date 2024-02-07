package com.ohgiraffers.section03.bidirection;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/*
* 양방향 매핑에서 어느 한 쪽이 연관 관계의 주인이 되면, 주인이 아닌 쪽에서는 속성을 지정해주어야 한다.
* 이때, 연관 관계의 주인이 아닌 객체 MappedBy를 써서 연관 관계 주인 객체의 필드명을 매핑 시켜 놓으면 양방향 관계를 적용할 수 있다.
* */
@Entity(name = "bidirection_category")
@Table(name = "tbl_category")
public class Category {

    @Id
    @Column(name = "category_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryCode;

    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "ref_category_code")
    private String refCategoryCode;

//    @OneToMany(mappedBy = "categoryCode") // mappedBy : 주인이 아닌 쪽에 설정, 주인이 아닌 쪽에서 주인은 조회만 가능하다. (본인은 가능)
//    private List<Menu> menuList = new ArrayList<>();

    // 주인_아닌쪽_영속성_테스트
    // cascade = CascadeType.PERSIST 영속성 전이로 주인 CUD 가능
    @OneToMany(mappedBy = "categoryCode", cascade = CascadeType.PERSIST)
    private List<Menu> menuList = new ArrayList<>();


    public Category() {
    }

    public Category(int categoryCode, String categoryName, String refCategoryCode) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.refCategoryCode = refCategoryCode;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getRefCategoryCode() {
        return refCategoryCode;
    }

    public void setRefCategoryCode(String refCategoryCode) {
        this.refCategoryCode = refCategoryCode;
    }

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {

//        List<Menu> newlist = new ArrayList<>();
//        for (Menu m : menuList) {
//            m.setCategoryCode(this.categoryCode);
//            newlist.add(m);
//        }

        this.menuList = menuList;
    }

    @Override
    public String toString() {
        return "CategoryAndMenu{" +
                "categoryCode=" + categoryCode +
                ", categoryName='" + categoryName + '\'' +
                ", refCategoryCode='" + refCategoryCode + '\'' +
                ", menuList=" + menuList +
                '}';
    }
}
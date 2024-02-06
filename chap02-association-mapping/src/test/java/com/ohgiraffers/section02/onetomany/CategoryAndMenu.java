package com.ohgiraffers.section02.onetomany;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "category_and_menu")
@Table(name = "tbl_category")
public class CategoryAndMenu {

    @Id
    @Column(name = "category_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryCode;

    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "ref_category_code")
    private String refCategoryCode;

//    @JoinColumn(name = "category_code")
//    @OneToMany(cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "category_code")
//    @ManyToOne
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "categoryCode")
    private List<Menu> menuList = new ArrayList<>(); // menuList가 필요한이유? 자바 객체의 관점. db에서는 무결성 위배이므로 필요가 없음


    public CategoryAndMenu() {
    }

    public CategoryAndMenu(int categoryCode, String categoryName, String refCategoryCode) {
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
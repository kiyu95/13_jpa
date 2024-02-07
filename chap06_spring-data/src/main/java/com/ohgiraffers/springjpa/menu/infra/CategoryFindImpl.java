package com.ohgiraffers.springjpa.menu.infra;

import com.ohgiraffers.springjpa.category.entity.Category;
import com.ohgiraffers.springjpa.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryFindImpl implements CategoryFind{

    @Autowired
    CategoryService categoryService;

    @Override
    public Integer getCategory(int code) {
        System.out.println("fsafsafsafsa  :  "+code);
        return categoryService.findByCategory(code);
    }
}

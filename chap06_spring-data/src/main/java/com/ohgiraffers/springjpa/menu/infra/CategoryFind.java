package com.ohgiraffers.springjpa.menu.infra;

import org.springframework.stereotype.Service;

@Service
public interface CategoryFind {

    Integer getCategory(int code);
}

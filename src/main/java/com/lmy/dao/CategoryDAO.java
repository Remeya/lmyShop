package com.lmy.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lmy.pojo.Category;

public interface CategoryDAO extends JpaRepository<Category,Integer>{

}

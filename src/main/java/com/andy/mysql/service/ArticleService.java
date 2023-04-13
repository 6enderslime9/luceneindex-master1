package com.andy.mysql.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.andy.lucene.entity.Article;

public interface ArticleService {
	
	public  List<Article> findByTitleLike(String title);
	public Article findById(Integer id);
    public List<Article> findAll();
    public Article add(Article article);
    public Article update(Article article);
    public void deleteById(Integer eid);
    public void deleteAll();
    public Long getMaxId();
    public Page<Article> findByPage(Pageable pageable);
}

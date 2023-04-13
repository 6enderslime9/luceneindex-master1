package com.andy.mysql.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.andy.lucene.entity.Article;
import com.andy.mysql.reporsity.ArticleRepository;
import com.andy.mysql.service.ArticleService;

@Service
public class ArticleServiceImpl implements ArticleService{

	 @Autowired
	 ArticleRepository repository;
	 
	@Override
	public List<Article> findByTitleLike(String title) {
		// TODO Auto-generated method stub
		List<Article> articles=repository.findByTitleLike(title);
	     return  articles;
	}

	@Override
	public Article findById(Integer id) {
		// TODO Auto-generated method stub
		 Optional<Article> article=repository.findById(id);
	     return  article.get();
	}

	@Override
	public List<Article> findAll() {
		// TODO Auto-generated method stub
		return repository.findAll();
	}

	@Override
	public Article add(Article article) {
		// TODO Auto-generated method stub
	   Article res=repository.save(article);
	   return  res;
	}

	@Override
	public Article update(Article article) {
		// TODO Auto-generated method stub
		 Article res=repository.save(article);
		 return  res;
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		repository.deleteById(id);
		
	}
	@Override
	public void deleteAll() {
		repository.deleteAll();
	}
	
	@Override
	public Long getMaxId() {
		return repository.count();
	}
	
	/**
     * 分页
     * @param pageable
     * @return
     */
	@Override
    public Page<Article> findByPage(Pageable pageable){
        return repository.findAll(pageable);
    }
}

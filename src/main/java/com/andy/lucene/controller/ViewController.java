package com.andy.lucene.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.andy.lucene.entity.Article;
import com.andy.mysql.service.ArticleService;

@Controller
public class ViewController {
    
    @Autowired
    private ArticleService articleService;
    
    /**
     *
     * @return  主页视图
     */
    @RequestMapping("/view")
    public String view(Model model,int id) {
    	System.out.println("id:"+id);
    	 Article article=articleService.findById(id);
         model.addAttribute("title",article.getTitle());
         model.addAttribute("author",article.getSign());
         model.addAttribute("content",article.getText());
         model.addAttribute("website",article.getHref());
         return "content.html";
    }

  
}

package com.andy.lucene.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.andy.lucene.entity.Article;
import com.andy.lucene.entity.Message;
import com.andy.lucene.entity.Record;
import com.andy.lucene.entity.Result;
import com.andy.lucene.service.PageService;
import com.andy.mysql.service.ArticleService;
import com.example.netdb.SubClientDnfg;
import com.example.netdb.SubClientDsxx;
import com.example.netdb.SubClientDyjytx;
import com.example.netdb.SubClientZzgz;

@Controller
public class PageController {
    @Resource
    private PageService pageService;
    
    @Autowired
    private ArticleService articleService;
    
    /**
     *
     * @return  主页视图
     */
    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    @ResponseBody
    @RequestMapping("/loadSite")
    public void loadSite() throws IOException {
    	pageService.indexInit();
    	
    	//1
    	SubClientZzgz client1=new  SubClientZzgz();
    	ConcurrentMap<Long, Message> meaasges1=client1.getTitles();
		for(Message value : meaasges1.values()) {
			 String subUrl=value.getHref();
			 if(subUrl.contains("ldhd")||subUrl.contains("gzdt")||subUrl.contains("cj")||subUrl.contains("cj")||subUrl.contains("dntjgb")) {
				 String classes=subUrl.substring(0, subUrl.length()-1);
				 System.out.println(classes.substring(classes.lastIndexOf("/")+1));
				 List<Article> articles=client1.getContent(classes.substring(classes.lastIndexOf("/")+1),value.getHref());
				 for(int j=0;j<articles.size();j++) {
					 Article article=articles.get(j);
					 Article res=articleService.add(article);
					 if(res!=null) {
						 pageService.addIndex(article);
					 }
				 }
			 }
		}
		
		//2
		SubClientDyjytx client2=new  SubClientDyjytx();
    	ConcurrentMap<Long, Message> meaasges2=client2.getTitles();
		for(Message value : meaasges2.values()) {
			 String subUrl=value.getHref();
			 if(subUrl.contains("zcwj")||subUrl.contains("zygz")||subUrl.contains("dxal")) {
				 String classes=subUrl.substring(0, subUrl.length()-1);
				 System.out.println(classes.substring(classes.lastIndexOf("/")+1)+"\t"+value.getHref());
				 List<Article> articles=client2.getContents(classes.substring(classes.lastIndexOf("/")+1),value.getHref());
				 for(int j=0;j<articles.size();j++) {
					 Article article=articles.get(j);
					 Article res=articleService.add(article);
					 if(res!=null) {
						 pageService.addIndex(article);
					 }
				 }
			 }
		}
		
		//3
		SubClientDsxx client3=new  SubClientDsxx();
    	ConcurrentMap<Long, Message> meaasges3=client3.getTitles();
		for(Message value : meaasges3.values()) {
			 String subUrl=value.getHref();
			 if(subUrl.contains("bs")||subUrl.contains("dt")||subUrl.contains("pl")) {
				 String classes=subUrl.substring(0, subUrl.length()-1);
				 System.out.println(classes.substring(classes.lastIndexOf("/")+1)+"\t"+value.getHref());
				 List<Article> articles=client3.getContents(classes.substring(classes.lastIndexOf("/")+1),value.getHref());
				 for(int j=0;j<articles.size();j++) {
					 Article article=articles.get(j);
					 Article res=articleService.add(article);
					 if(res!=null) {
						 pageService.addIndex(article);
					 }
				 }
			 }
		}
		
		//4
		SubClientDnfg client4=new  SubClientDnfg();
    	ConcurrentMap<Long, Message> meaasges4=client4.getTitles();
		for(Message value : meaasges4.values()) {
			 String subUrl=value.getHref();
			 if(!subUrl.contains("zcwj")) {
				 String classes=subUrl.substring(0, subUrl.length()-1);
				 System.out.println(classes.substring(classes.lastIndexOf("/")+1)+"\t"+value.getHref());
				 List<Article> articles=client4.getContents(classes.substring(classes.lastIndexOf("/")+1),value.getHref());
				 for(int j=0;j<articles.size();j++) {
					 Article article=articles.get(j);
					 Article res=articleService.add(article);
					 if(res!=null) {
						 pageService.addIndex(article);
					 }
				 }
			 }
		}
		
		pageService.closeIndexWriter();
    }
    /**
     * 初始化索引数据
     * @return  成功或者失败的消息
     * @throws IOException 
     */
    @ResponseBody
    @RequestMapping("/indexInit")
    public Result indexInit() throws IOException {
    	pageService.indexInit();
    	Long count=articleService.getMaxId();
    	long maxPage=count/20;
    	for(int page=0;page<maxPage;page++) {
    		//根据id，倒序
    		Pageable pageable = new PageRequest(page, 20, new Sort(new Sort.Order(Sort.Direction.DESC,"id")));
    		List<Article> articles=articleService.findByPage(pageable).getContent();
    		for(int i=0;i<articles.size();i++) {
    			pageService.addIndex(articles.get(i));
    		}
    	}
    	pageService.closeIndexWriter();
        return new Result("索引建立成功！当前搜索可用！");
    }
    
    /**
     * 响应搜索请求
     * @return  list集合的数据
     */
    @ResponseBody
    @RequestMapping(value = "/searchLuncene", method = RequestMethod.POST)
    public List<Record> searchLuncene(@RequestParam String keyword) throws IOException {
        return pageService.searchLuncene(keyword);
    }
}

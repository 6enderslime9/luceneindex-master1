package com.example.netdb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.andy.lucene.entity.Article;
import com.andy.lucene.entity.Message;

/*
 * 党员教育		https://www.12371.cn/dyjytx/
 */

public class SubClientDyjytx {
    public ConcurrentMap<Long, Message> meaasges = new ConcurrentHashMap<>();
    public CloseableHttpResponse response = null;
    public String Url = "https://www.12371.cn/dyjytx/";

    public static void main(String[] args) {
        SubClientDyjytx client = new SubClientDyjytx();
        ConcurrentMap<Long, Message> meaasges = client.getTitles();
        for (Message value : meaasges.values()) {
            String subUrl = value.getHref();
            if (subUrl.contains("zcwj") || subUrl.contains("zygz") || subUrl.contains("dxal")) {
                String classes = subUrl.substring(0, subUrl.length() - 1);
//				 System.out.println(classes.substring(classes.lastIndexOf("/")+1)+"\t"+value.getHref());
                client.getContents(classes.substring(classes.lastIndexOf("/") + 1), value.getHref());
            }
        }
    }

    public ConcurrentMap<Long, Message> getTitles() {
        try {
            //3.执行get请求，相当于在输入地址栏后敲回车键
            response = HttpClientPool.httpClient.execute(HttpClientPool.getHttpConn(Url));
            //4.判断响应状态为200，进行处理
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //5.获取响应内容
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
                //6.Jsoup解析html
                Document document = Jsoup.parse(html);
                //像js一样，通过标签获取title
                System.out.println(document.getElementsByTag("title").first());
                //像js一样，通过id 获取文章列表元素对象
                Elements postList = document.getElementsByClass("dyw774_logo");
                System.out.println("postList:" + postList.size());
                //像js一样，通过class 获取列表下的所有博客
//                Elements postItems = postList.getElementsByClass("post_item");
                Elements postItems = postList.select("a");
                //循环处理每篇博客
                for (Element postItem : postItems) {
                    //像jquery选择器一样，获取文章标题元素
                    Elements titleEle = postItem.select("a");
                    System.out.println(postItem.siblingIndex() + "、" + titleEle.text() + "\t" + titleEle.attr("href"));
                    ;
                    meaasges.put(Long.valueOf(postItem.siblingIndex()), new Message(Long.valueOf(postItem.siblingIndex()), titleEle.text(), titleEle.attr("href")));
                }

            } else {
                //如果返回状态不是200，比如404（页面不存在）等，根据情况做处理，这里略
                System.out.println("返回状态不是200");
                System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //6.关闭
//            HttpClientUtils.closeQuietly(response);
//            HttpClientUtils.closeQuietly(HttpClientPool.httpClient);
        }

        return meaasges;
    }


    public List<Article> getContents(String classes, String subUrl) {
        List<Article> contents = new ArrayList<Article>();
        try {
            response = HttpClientPool.httpClient.execute(HttpClientPool.getHttpConn(subUrl));
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //5.获取响应内容
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
                Document document = Jsoup.parse(html);
                Elements postList = document.getElementsByClass("showMoreNChildren");
                Elements postItems = postList.select("li");
                //循环处理每篇博客
                for (Element postItem : postItems) {
                    Elements titleEle = postItem.select("a");
//	                    String times = postItem.getElementsByClass("times").get(0).text();
//	                    System.out.println(titleEle.text()+"\t"+ titleEle.attr("href"));
                    if (!titleEle.attr("href").contains("special") && !titleEle.attr("href").contains("cxsm/zywj")) {
                        CloseableHttpResponse detailResponse = HttpClientPool.httpClient.execute(HttpClientPool.getHttpConn(titleEle.attr("href")));
                        if (detailResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            //5.获取响应内容
                            try {
                                HttpEntity detailEntity = detailResponse.getEntity();
                                String detailHtml = EntityUtils.toString(detailEntity, "utf-8");
                                Document detailDocument = Jsoup.parse(detailHtml);
                                String title = detailDocument.getElementsByClass("big_title").get(0).text();
                                String content = detailDocument.getElementsByClass("word").get(0).text();
                                String sign = detailDocument.getElementsByClass("time").size() > 0 ? detailDocument.getElementsByClass("time").get(0).text() : null;
//	                    			System.out.println(classes+ "\t" + title+"\t"+ titleEle.attr("href")+"\t"+sign+"\t"+content);
                                contents.add(new Article(classes, titleEle.attr("href"), title, "", content, sign));
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.err.println(titleEle.attr("href"));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return contents;
    }
}

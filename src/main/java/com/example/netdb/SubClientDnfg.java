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
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/*
 * 党章党规		https://www.12371.cn/special/dnfg/
 */

public class SubClientDnfg {
    public ConcurrentMap<Long, Message> meaasges = new ConcurrentHashMap<>();
    public CloseableHttpResponse response = null;
    public String Url = "https://www.12371.cn/special/dnfg/";

    public static void main(String[] args) {
        SubClientDnfg client = new SubClientDnfg();
        ConcurrentMap<Long, Message> meaasges = client.getTitles();
        for (Message value : meaasges.values()) {
            String subUrl = value.getHref();
            if (!subUrl.contains("zcwj")) {
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
                Elements postList = document.getElementsByClass("dyw1015_nav");
                System.out.println("postList:" + postList.size());
                //像js一样，通过class 获取列表下的所有博客
//                Elements postItems = postList.getElementsByClass("post_item");
                Elements postItems = postList.select("a");
                //循环处理每篇博客
                long index = 0;
                for (Element postItem : postItems) {
                    //像jquery选择器一样，获取文章标题元素
                    Elements titleEle = postItem.select("a");
//                    System.out.println(postItem.siblingIndex()+ "、" + titleEle.text()+"\t"+ titleEle.attr("href"));;
                    meaasges.put(index++, new Message(Long.valueOf(postItem.siblingIndex()), titleEle.text(), titleEle.attr("href")));
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


    @SuppressWarnings("resource")
    public List<Article> getContents(String classes, String subUrl) {
        List<Article> contents = new ArrayList<Article>();
        try {
            WebClient webClient = new WebClient(BrowserVersion.CHROME);
            //其他报文头字段可以根据需要添加
            webClient.getCookieManager().setCookiesEnabled(false);//开启cookie管理
            webClient.getOptions().setJavaScriptEnabled(true);//开启js解析。对于动态网页，这个是必须的
            webClient.getOptions().setThrowExceptionOnScriptError(true);//开启js解析。对于动态网页，这个是必须的
            webClient.setJavaScriptTimeout(100000);//设置JS执行的超时时间
            webClient.getOptions().setCssEnabled(false);//开启css解析。对于变态网页，这个是必须的。
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());//设置支持AJAX
            webClient.getOptions().setTimeout(15000);
            HtmlPage rootPage = webClient.getPage(subUrl);
            Document document = Jsoup.parse(rootPage.asXml());
//	        System.err.println(document);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //5.获取响应内容
                Elements postList = document.getElementsByClass("dyw1015_list");
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
                                System.out.println(classes + "\t" + title + "\t" + titleEle.attr("href") + "\t" + sign + "\t" + content);
                                contents.add(new Article(classes, titleEle.attr("href"), title, "", content, sign));
                            } catch (Exception e) {
                                //e.printStackTrace();
                                System.err.println(titleEle.attr("href"));
                            }
                        }
                    }
                }
            }
        } catch (com.gargoylesoftware.htmlunit.ScriptException e) {
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return contents;
    }
}

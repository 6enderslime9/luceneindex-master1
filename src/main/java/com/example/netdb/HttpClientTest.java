package com.example.netdb;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.andy.lucene.entity.Message;

/*
 * 爬虫类：https://www.12371.cn
 */
/*
 * 中国共产党党内文献知识库及检索系统的设计与实现
 * （1）抓取目标 https://www.12371.cn
 * （2）设计数据库结构，将抓取的数据持久化，包含：标题，URL，内容，爬取时间，MD5（或其他加密）
 * （3）设计知识库结构，设计数据提取算法。提取出来某个文章的发文单位，正文，发文时间之类的，然后存在知识库里
 * （4）设计检索系统，命名实体识别基于Double Array Trie 树来实现 技术：Java，SpringBoot，MySQL，Lucene
 * https://blog.csdn.net/newlw/article/details/125946886
 */
public class HttpClientTest {
    public ConcurrentMap<Long, Message> meaasges = new ConcurrentHashMap<>();
    public CloseableHttpResponse response = null;
    public String Url = "https://www.12371.cn/";

    public static void main(String[] args) {
        new HttpClientTest();
    }

    //      中国共产党党内文献知识库及检索系统的设计与实现
//    	1、组织工作		https://news.12371.cn/dzybmbdj/zzb/
//    	3、党员教育		https://www.12371.cn/dyjytx/
//    	5、思想理论		https://www.12371.cn/special/sxll/
//    	7、党章党规		https://www.12371.cn/special/dnfg/
//    	9、党的历史		https://www.12371.cn/dsxx/
//    	11、先进典型	https://www.12371.cn/special/dzby/
//    	13、示范培训	https://www.12371.cn/special/qgdyjypxsfb/sfb/
//    	15、观摩交流	https://www.12371.cn/special/gmjl/dsp/
//    	17、课件资源	https://www.12371.cn/special/kjzy/
//    	19、支部生活	https://www.12371.cn/special/zbsh/
//    	21、党务问答	https://wenda.12371.cn/
//    	23、先锋文汇	https://tougao.12371.cn/
    public HttpClientTest() {
        try {
            //3.执行get请求，相当于在输入地址栏后敲回车键
            response = HttpClientPool.httpClient.execute(HttpClientPool.getHttpConn(Url));
            //4.判断响应状态为200，进行处理
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //5.获取响应内容
                HttpEntity httpEntity = response.getEntity();
                String html = EntityUtils.toString(httpEntity, "utf-8");
//                System.out.println(html);
                //6.Jsoup解析html
                Document document = Jsoup.parse(html);
                //像js一样，通过标签获取title
                System.out.println(document.getElementsByTag("title").first());
                //像js一样，通过id 获取文章列表元素对象
//                Element postList = document.getElementById("post_list");
//                Elements column_wrapper_1200 = document.getElementsByClass("column_wrapper_1200");
                Elements postList = document.getElementsByClass("dyw947_nav");
                System.out.println("postList:" + postList.size());
                //像js一样，通过class 获取列表下的所有博客
//                Elements postItems = postList.getElementsByClass("post_item");
                Elements postItems = postList.select("li");
//                System.out.println("postItems:" +postItems);
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
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //6.关闭
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(HttpClientPool.httpClient);
        }
    }

    public ConcurrentMap<Long, Message> getMeaasges() {
        //方法2
        ConcurrentHashMap<Long, Message> collect = meaasges.entrySet().stream().sorted(new Comparator<Map.Entry<Long, Message>>() {
            @Override
            public int compare(Map.Entry<Long, Message> o1, Map.Entry<Long, Message> o2) {
                return Long.valueOf(o1.getValue().getId()).compareTo(Long.valueOf(o2.getValue().getId()));
            }
        }).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, ConcurrentHashMap::new));

        return collect;
    }

    public void setMeaasges(ConcurrentMap<Long, Message> meaasges) {
        this.meaasges = meaasges;
    }
}

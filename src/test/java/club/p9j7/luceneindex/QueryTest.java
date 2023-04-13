package club.p9j7.luceneindex;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.andy.LuceneindexApplication;
import com.andy.lucene.entity.Record;
import com.andy.lucene.service.PageService;

import org.apache.lucene.document.Document;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LuceneindexApplication.class)
public class QueryTest {
    @Autowired
    PageService pageService;
    @Test
    public void testPageService() throws IOException {
       // pageService.searchFiles("li");
        System.out.println(System.getProperty("user.home"));
//        pageService.indexFiles();

//        try {
//            List<Record> document = pageService.searchFiles("aaaa");
//            System.err.println("11111="+document.get(0));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}

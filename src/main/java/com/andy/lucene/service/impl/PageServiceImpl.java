package com.andy.lucene.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.andy.lucene.entity.Article;
import com.andy.lucene.entity.Record;
import com.andy.lucene.entity.Result;
import com.andy.lucene.service.PageService;

@Service
public class PageServiceImpl implements PageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //源文件存放位置为用户主目录下的 lucenedir 文件夹
    private final String docsPath = System.getProperty("user.home") + "/lucenedir";
    //索引文件存放位置用户主目录下的 luceneidx 文件夹
    private final String indexPath = System.getProperty("user.home") + "/luceneidx";
    public static IndexWriter indexWriter = null;

    @Override
    public Result indexInit() {
        Path luceneindexDir = Paths.get(indexPath);
        //如果路径错误退出程序并且打印日志
        if (!Files.isReadable(luceneindexDir)) {
            logger.error(luceneindexDir.toString() + "路径错误!");
            //System.exit(1);
        }
        long start = System.currentTimeMillis();
        try {
            //设置索引存放的位置；设置在当前目录中
            Directory directory = FSDirectory.open(luceneindexDir);
            //Analyzer analyzer = new StandardAnalyzer(); // 标准分词器，用于英文
            //Analyzer analyzer = new SmartChineseAnalyzer();//中文分词
            //Analyzer analyzer = new ComplexAnalyzer();//中文分词
            //Analyzer analyzer = new IKAnalyzer();//中文分词
            Analyzer analyzer = new StandardAnalyzer();//中文分词
            //创建索引写入配置
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            //配置索引创建类
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            indexWriter = new IndexWriter(directory, indexWriterConfig);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //出现错误,返回失败的 result 信息
            return new Result("索引建立失败!请联系软件维护人员!");
        } finally {
        }

        long end = System.currentTimeMillis();
        System.out.println("索引花费了" + (end - start) + " 毫秒");
        //如果索引建立完成,则返回成功的 result 信息
        return new Result("索引建立成功！当前搜索可用！");
    }

    @Override
    public void closeIndexWriter() throws IOException {
        //关闭流
        indexWriter.close();
    }

    @Override
    public Result addIndex(Article article) {
        long start = System.currentTimeMillis();
        try {
            //创建Document对象，存储索引
            Document doc = new Document();
            //将字段加入到doc中
//	        Student = new Student("张三","00001","男");
//	        doc.add(new StringField("name", student.getName(),Field.Store.YES));
//	        doc.add(new StringField("studentId", student.getStudentId(), Field.Store.YES));
//	        doc.add(new StringField("sex", student.getSex(), Field.Store.YES));
            doc.add(new StringField("id", article.getId().toString(), Field.Store.YES));
            doc.add(new StringField("classes", article.getClasses(), Field.Store.YES));
            doc.add(new TextField("title", article.getTitle(), Field.Store.YES));
            doc.add(new TextField("text", article.getText(), Field.Store.YES));
            doc.add(new LongPoint("created", article.getCreated() == null ? 0 : Long.parseLong(article.getCreated())));
            doc.add(new StringField("sign", article.getSign() == null ? "——" : article.getSign(), Field.Store.YES));
            doc.add(new StringField("href", article.getHref() == null ? "——" : article.getHref(), Field.Store.YES));

            //将doc对象保存到索引库中
            indexWriter.addDocument(doc);
            indexWriter.commit();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //出现错误,返回失败的 result 信息
            return new Result("索引建立失败!请联系软件维护人员!");
        } finally {
        }

        long end = System.currentTimeMillis();
        System.out.println("索引花费了" + (end - start) + " 毫秒");
        //如果索引建立完成,则返回成功的 result 信息
        return new Result("索引建立成功！当前搜索可用！");
    }


    /**
     * 按词条搜索
     * <p>
     * TermQuery是最简单、也是最常用的Query。TermQuery可以理解成为“词条搜索”，
     * 在搜索引擎中最基本的搜索就是在索引中搜索某一词条，而TermQuery就是用来完成这项工作的。
     * 在Lucene中词条是最基本的搜索单位，从本质上来讲一个词条其实就是一个名/值对。
     * 只不过这个“名”是字段名，而“值”则表示字段中所包含的某个关键字。
     * 搜索执行类
     */
    @Override
    public List<Record> searchLuncene(String keyword) {
        //定义索引读取器
        logger.info("检索值: " + keyword);
        IndexReader indexReader = null;
        try {
            indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
        } catch (IOException e) {
            logger.error("索引文件路径发生错误!");
            e.printStackTrace();
        }
        //定义索引搜索器
        IndexSearcher searcher = new IndexSearcher(indexReader);
        //转为小写否则出错
        keyword = keyword.toLowerCase();
        String searchField = "title";
//        Term aTerm = new Term("name", keyword);
//        Query query = new TermQuery(aTerm);
        Query query = new WildcardQuery(new Term(searchField, "*" + keyword + "*"));  //模糊搜索
        TopDocs topDocs;
        ScoreDoc[] scoreDocs = null;
        try {
//        	SortField sortField = new SortField("title", SortField.Type.STRING,false);
//            Sort sort = new Sort(sortField);
            //定义缓存10000条记录
            topDocs = searcher.search(query, 100000, Sort.RELEVANCE);
            logger.info("符合条件的记录总数为：" + topDocs.totalHits);
            //缓存的1000条记录
            scoreDocs = topDocs.scoreDocs;
        } catch (IOException e1) {
            logger.error("字段搜索过程中出错!");
            e1.printStackTrace();
        }


        //将查询到的前1000条记录加入 list<Record>
        List<Record> recordList = new ArrayList<>(scoreDocs.length);
        for (ScoreDoc scoreDoc : scoreDocs) {
            //根据id查询防止返回重复数据
            Document doc = null;
            try {
                doc = searcher.doc(scoreDoc.doc);
            } catch (IOException e) {
                logger.error("字段遍历过程中出错!");
                e.printStackTrace();
            }
            Record record = new Record(Integer.parseInt(doc.get("id")), doc.get("classes"), doc.get("title"), doc.get("text"), doc.get("created"), doc.get("sign"), doc.get("href"));
            recordList.add(record);
        }
        logger.info("recordList创建完成！实际大小为：" + recordList.size());
        return recordList;
    }
}



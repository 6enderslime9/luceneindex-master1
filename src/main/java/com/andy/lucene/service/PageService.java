package com.andy.lucene.service;


import java.io.IOException;
import java.util.List;

import com.andy.lucene.entity.Article;
import com.andy.lucene.entity.Record;
import com.andy.lucene.entity.Result;

/**
 * 系统服务接口
 */
public interface PageService {
    /**
     * 文件索引方法
     * @return  建立成功与否的消息
     */
    Result indexInit();
    Result addIndex(Article article);
    void closeIndexWriter() throws IOException ;

    /**
     * 索引查询方法
     * @param keyword
     * @return  查询到的记录集合
     * @throws IOException
     */
    List<Record> searchLuncene(String keyword) throws IOException;
}

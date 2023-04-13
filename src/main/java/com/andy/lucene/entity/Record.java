package com.andy.lucene.entity;

/**
 * 查询到的某条记录
 */
public class Record {
    private int id;
    private String classes; //类型
    private String title;   //标题
    private String text;    //内容
    private String created; //发布时间
    private String sign;    //落款
    private String content;
    private String href; //链接地址

    public Record(int id, String classes, String title, String text, String created, String sign, String href) {
        this.id = id;
        this.classes = classes;
        this.title = title;
        this.text = text;
        this.created = created;
        this.sign = sign;
        this.href = href;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getClasses() {
        return classes;
    }


    public void setClasses(String classes) {
        this.classes = classes;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }


    public String getCreated() {
        return created;
    }


    public void setCreated(String created) {
        this.created = created;
    }


    public String getSign() {
        return sign;
    }


    public void setSign(String sign) {
        this.sign = sign;
    }


    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }


    public String getHref() {
        return href;
    }


    public void setHref(String href) {
        this.href = href;
    }


//	@Override
//    public String toString() {
//        return "Record{" +
//                "id=" + id +
//                ", path='" + path + '\'' +
//                ", content='" + content + '\'' +
//                '}';
//    }
}

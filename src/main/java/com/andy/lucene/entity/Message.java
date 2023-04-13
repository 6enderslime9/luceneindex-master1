package com.andy.lucene.entity;

import java.util.Calendar;

// import javax.validation.constraints.Pattern;


public class Message {

    private Long id;

    private String title;
    
    //@Range(min = 0, max = 10)
//    @Size(min = 5, max = 30, message = "Href内容长度需要在{min}-{max}之间")
    private String href;
    
    //@Pattern(regexp="\\d+")
    private String text;
    


    private Calendar created = Calendar.getInstance();

    public Message(Long id,String title,String href) {
    	this.id=id;
    	this.title=title;
    	this.href=href;
    }
    
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getCreated() {
        return this.created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}

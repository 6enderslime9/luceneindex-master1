package com.andy.lucene.entity;

/**
 * 建立索引时返回的消息类
 */
public class Result {
    private String message;

    public Result(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "message='" + message + '\'' +
                '}';
    }
}

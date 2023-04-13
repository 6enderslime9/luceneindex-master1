package com.andy.lucene.entity;

public class Student {
	public String name;
	public String studentId;
	public String sex;
	
	public Student(String name,String studentId,String sex) {
		this.name=name;	
		this.studentId=studentId;	
		this.sex=sex;	
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	
}
	
	

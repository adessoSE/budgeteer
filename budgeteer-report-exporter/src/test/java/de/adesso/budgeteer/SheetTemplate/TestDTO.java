package de.adesso.budgeteer.SheetTemplate;

import java.util.Date;
import java.util.List;

public class TestDTO {
	private String test;
	private double foo;
	private boolean bar;
	private Date date;
	private List<Attribute> dynamic;
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	public double getFoo() {
		return foo;
	}
	public void setFoo(double foo) {
		this.foo = foo;
	}
	public boolean isBar() {
		return bar;
	}
	public void setBar(boolean bar) {
		this.bar = bar;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public List<Attribute> getDynamic() {
		return dynamic;
	}
	public void setDynamic(List<Attribute> dynamic) {
		this.dynamic = dynamic;
	}
}
package org.techtown.calendar;

/**
 * 일자 정보를 담기 위한 클래스 정의
 * 
 * @author Mike
 *
 */
public class MonthItem {

	private int dayValue;

	public MonthItem() {
		
	}
	
	public MonthItem(int day) {
		dayValue = day;
	}
	
	public int getDay() {
		return dayValue;
	}

	public void setDay(int day) {
		this.dayValue = day;
	}
	
	
	
}

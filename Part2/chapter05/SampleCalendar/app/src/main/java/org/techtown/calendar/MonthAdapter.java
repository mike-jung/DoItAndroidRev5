package org.techtown.calendar;

import android.content.Context;
import android.graphics.Color;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.Calendar;

/**
 * 어댑터 객체 정의
 * 
 * @author Mike
 *
 */
public class MonthAdapter extends BaseAdapter {

	public static final String TAG = "MonthAdapter";
	
	Context mContext;
	
	public static int oddColor = Color.rgb(225, 225, 225);
	public static int headColor = Color.rgb(12, 32, 158);
	
	private int selectedPosition = -1;
	
	private MonthItem[] items;
	
	private int countColumn = 7;
	
	int mStartDay;
	int startDay;
	int curYear;
	int curMonth;
	
	int firstDay;
	int lastDay;
	
	Calendar mCalendar;
	boolean recreateItems = false;
	
	public MonthAdapter(Context context) {
		super();

		mContext = context;
		
		init();
	}
	
	public MonthAdapter(Context context, AttributeSet attrs) {
		super();

		mContext = context;
		
		init();
	}

	private void init() {
		items = new MonthItem[7 * 6];

		mCalendar = Calendar.getInstance();
		recalculate();
		resetDayNumbers();
		
	}
	
	public void recalculate() {

		// set to the first day of the month
		mCalendar.set(Calendar.DAY_OF_MONTH, 1);
		
		// get week day
		int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
		firstDay = getFirstDay(dayOfWeek);
		Log.d(TAG, "firstDay : " + firstDay);
		
		mStartDay = mCalendar.getFirstDayOfWeek();
		curYear = mCalendar.get(Calendar.YEAR);
		curMonth = mCalendar.get(Calendar.MONTH);
		lastDay = getMonthLastDay(curYear, curMonth);
		
		Log.d(TAG, "curYear : " + curYear + ", curMonth : " + curMonth + ", lastDay : " + lastDay);
		
		int diff = mStartDay - Calendar.SUNDAY - 1;
        startDay = getFirstDayOfWeek();
		Log.d(TAG, "mStartDay : " + mStartDay + ", startDay : " + startDay);
		
	}
	
	public void setPreviousMonth() {
		mCalendar.add(Calendar.MONTH, -1);
        recalculate();
        
        resetDayNumbers();
        selectedPosition = -1;
	}
	
	public void setNextMonth() {
		mCalendar.add(Calendar.MONTH, 1);
        recalculate();
        
        resetDayNumbers();
        selectedPosition = -1;
	}
	
	public void resetDayNumbers() {
		for (int i = 0; i < 42; i++) {
			// calculate day number
			int dayNumber = (i+1) - firstDay;
			if (dayNumber < 1 || dayNumber > lastDay) {
				dayNumber = 0;
			}
			
	        // save as a data item
	        items[i] = new MonthItem(dayNumber);
		}
	}
	
	private int getFirstDay(int dayOfWeek) {
		int result = 0;
		if (dayOfWeek == Calendar.SUNDAY) {
			result = 0;
		} else if (dayOfWeek == Calendar.MONDAY) {
			result = 1;
		} else if (dayOfWeek == Calendar.TUESDAY) {
			result = 2;
		} else if (dayOfWeek == Calendar.WEDNESDAY) {
			result = 3;
		} else if (dayOfWeek == Calendar.THURSDAY) {
			result = 4;
		} else if (dayOfWeek == Calendar.FRIDAY) {
			result = 5;
		} else if (dayOfWeek == Calendar.SATURDAY) {
			result = 6;
		}
		
		return result;
	}
	
	
	public int getCurYear() {
		return curYear;
	}
	
	public int getCurMonth() {
		return curMonth;
	}
	
	
	public int getNumColumns() {
		return 7;
	}

	public int getCount() {
		return 7 * 6;
	}

	public Object getItem(int position) {
		return items[position];
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView(" + position + ") called.");

		MonthItemView itemView;
		if (convertView == null) {
			itemView = new MonthItemView(mContext);
		} else {
			itemView = (MonthItemView) convertView;
		}	
		
		// create a params
		GridView.LayoutParams params = new GridView.LayoutParams(
				GridView.LayoutParams.MATCH_PARENT,
				120);
		
		// calculate row and column
		int rowIndex = position / countColumn;
		int columnIndex = position % countColumn;
		
		Log.d(TAG, "Index : " + rowIndex + ", " + columnIndex);

		// set item data and properties
		itemView.setItem(items[position]);
		itemView.setLayoutParams(params);
		itemView.setPadding(2, 2, 2, 2);
		
		// set properties
		itemView.setGravity(Gravity.LEFT);
		
		if (columnIndex == 0) {
			itemView.setTextColor(Color.RED);
		} else {
			itemView.setTextColor(Color.BLACK);
		}
		
		// set background color
		if (position == getSelectedPosition()) {
        	itemView.setBackgroundColor(Color.YELLOW);
        } else {
        	itemView.setBackgroundColor(Color.WHITE);
        }
        

        
        
		return itemView;
	}

	
    /**
     * Get first day of week as android.text.format.Time constant.
     * @return the first day of week in android.text.format.Time
     */
    public static int getFirstDayOfWeek() {
        int startDay = Calendar.getInstance().getFirstDayOfWeek();
        if (startDay == Calendar.SATURDAY) {
            return Time.SATURDAY;
        } else if (startDay == Calendar.MONDAY) {
            return Time.MONDAY;
        } else {
            return Time.SUNDAY;
        }
    }
 
	
    /**
     * get day count for each month
     * 
     * @param year
     * @param month
     * @return
     */
    private int getMonthLastDay(int year, int month){
    	switch (month) {
 	   		case 0:
      		case 2:
      		case 4:
      		case 6:
      		case 7:
      		case 9:
      		case 11:
      			return (31);

      		case 3:
      		case 5:
      		case 8:
      		case 10:
      			return (30);

      		default:
      			if(((year%4==0)&&(year%100!=0)) || (year%400==0) ) {
      				return (29);   // 2월 윤년계산
      			} else { 
      				return (28);
      			}
 	   	}
 	}
    
    
    
	
	
	
	
	
	/**
	 * set selected row
	 */
	public void setSelectedPosition(int selectedPosition) {
		this.selectedPosition = selectedPosition;
	}

	/**
	 * get selected row
	 * 
	 * @return
	 */
	public int getSelectedPosition() {
		return selectedPosition;
	}
	

}

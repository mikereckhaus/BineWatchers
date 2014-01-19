package com.example.binewatchers;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DayCount {

	LinkedHashMap<String, Double> pointHistory;
	
	void persist  ()
	{
		PreferenceProvider.getInstance().setPointHistory(pointHistory); 
	}
	
	void addEntry(String date, Double points)
	{
		pointHistory.put(date, points);
		PreferenceProvider.getInstance().setPointHistory(pointHistory);
	}
	
	public void removeEntry(String date){
		pointHistory.remove(date);
		PreferenceProvider.getInstance().setPointHistory(pointHistory);
	}
	
	void restore()
	{
		pointHistory = PreferenceProvider.getInstance().getPointHistory();	
	}
	
	Double getUsedPoints()
	{
		Double consumedPoints = 0.0;
		for (Double value : pointHistory.values()) {
		    consumedPoints = consumedPoints + value;
		}
		return consumedPoints;
	}
	
	public HashMap<String, Double> getMap()
	{
		return pointHistory;
	}
	
	public void reset()
	{
		pointHistory.clear();
		PreferenceProvider.getInstance().setPointHistory(pointHistory);
	}
}

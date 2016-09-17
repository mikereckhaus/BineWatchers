package com.solutions.rockhouse.binewatchers;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Data model Of points Per Day 
 * @author Mike
 *
 */
public class PointsPerDay {

	LinkedHashMap<String, Double> pointsPerDay;
		
	void addEntry(String date, Double points)
	{
		pointsPerDay.put(date, points);
		PreferenceProvider.getInstance().setPointHistory(pointsPerDay);
	}
	
	public void removeEntry(String date){
		pointsPerDay.remove(date);
		PreferenceProvider.getInstance().setPointHistory(pointsPerDay);
	}
	
	void restore()
	{
		pointsPerDay = PreferenceProvider.getInstance().getPointHistory();	
	}
	
	Double getUsedPoints()
	{
		Double consumedPoints = 0.0;
		for (Double value : pointsPerDay.values()) {
		    consumedPoints = consumedPoints + value;
		}
		return consumedPoints;
	}
	
	public HashMap<String, Double> getMap()
	{
		return pointsPerDay;
	}
	
	public void reset()
	{
		pointsPerDay.clear();
		PreferenceProvider.getInstance().setPointHistory(pointsPerDay);
	}
}

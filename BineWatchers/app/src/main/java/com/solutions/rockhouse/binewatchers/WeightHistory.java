package com.solutions.rockhouse.binewatchers;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class WeightHistory {

	public TreeMap<Date, Double> weightHistory;
	
	public WeightHistory() {
		super();
	}
	
	void addEntry(Date date, Double weight)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    c.set(Calendar.MILLISECOND, 0);
	    
		weightHistory.put(c.getTime(), weight);
		PreferenceProvider.getInstance().setWeightHistory(weightHistory);
	}
	
	public void removeEntry(Date date){
		// only use year month and date
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    c.set(Calendar.MILLISECOND, 0);
		weightHistory.remove(c.getTime());
		PreferenceProvider.getInstance().setWeightHistory(weightHistory);
	}
	
	void restore()
	{
		weightHistory = PreferenceProvider.getInstance().getWeightHistory();	
	}
	
	public Map<Date, Double> getMap()
	{
		return weightHistory;
	}

	public Date getFirstDate() {
		Date result;
		if (weightHistory.keySet().iterator().hasNext() )
		{
			result =  weightHistory.keySet().iterator().next();
		}
		else
		{
			Calendar c = Calendar.getInstance();
			result = c.getTime();
		}
		
		return result;
	}

	public Date getLastDate() {
		Date result = new Date();
		Iterator<Date> current= weightHistory.keySet().iterator();
		// if not empty, iterate over all entries
		if ( current.hasNext() )
		{
			while (current.hasNext())
			{
				result = current.next();
			}
		}
		else
		{
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, c.get(Calendar.MONTH+1));
			result = c.getTime();
		}
			return result;
	}
}

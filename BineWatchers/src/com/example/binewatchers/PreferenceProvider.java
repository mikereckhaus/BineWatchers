package com.example.binewatchers;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.SharedPreferences;

/**
 * 
 * @author Mike
 *
 * Singleton class mit Gettern und Settern für einzelne Preferences
 */
public class PreferenceProvider {
	
	// preferences Identifiers
	public static final String CONSUMED_POINTS_ENTRY_COUNT = "ConsumedPointsEntryCount";
	public static final String CONSUME_DATE = "ConsumeDate";
	public static final String CONSUME_POINTS = "ConsumePoints";
	public static final String DAILY_POINTS = "DailyPoints";
	
	
	private static PreferenceProvider instance = new PreferenceProvider();
	private SharedPreferences mPreferences;
	
	public void setSharedPreferences(SharedPreferences prefs)
	{
		mPreferences = prefs;
	}
	
	private PreferenceProvider() {}
	
	public static PreferenceProvider getInstance()
	{
		return instance;
	}
	
	public void setPointHistory( HashMap<String, Double> pointHistory )
	{
		int consumeEntriesCount = pointHistory.size();
		SharedPreferences.Editor edit = mPreferences.edit();
		edit.putInt(CONSUMED_POINTS_ENTRY_COUNT, consumeEntriesCount);
		
		int i = 0;
		// speichere in preferences:
		for (Map.Entry<String, Double> entry : pointHistory.entrySet()) {
		    // Store date
			String key = entry.getKey();
		    String identifier = CONSUME_DATE;
		    identifier = identifier + i;
		    edit.putString(identifier, key);
		    
		    // Store Points
		    Double value = entry.getValue();
		    identifier = CONSUME_POINTS;
		    identifier = identifier + i;
		    
		    edit.putFloat(identifier, (value.floatValue()));
			i++;	    
		}
		
		edit.commit();
	}

	public LinkedHashMap<String, Double> getPointHistory( )
	{
		LinkedHashMap<String, Double> result = new LinkedHashMap<String, Double>();
		int defaultValue = 0;
		
		float defaultFloat = 0.0f;
		String defaultString = "";
		
		int consumeEntriesCount = mPreferences.getInt(CONSUMED_POINTS_ENTRY_COUNT, defaultValue);
		for (int i = 0; i < consumeEntriesCount; i++)
		{
			String date = mPreferences.getString(CONSUME_DATE + i, defaultString);
			Double points = Double.valueOf(mPreferences.getFloat(CONSUME_POINTS + i, defaultFloat));
			result.put(date, points);
		}
		
		return result;
	}
	
	public void setDailyPoints( int dailyPoints)
	{
		SharedPreferences.Editor edit = mPreferences.edit();
		edit.putInt(DAILY_POINTS, dailyPoints);
		edit.commit();
	}

	public int getDailyPoints( )
	{
		int defaultValue = 0;
		int result  = mPreferences.getInt(DAILY_POINTS, defaultValue);
		
		return result;
	}

}

package com.example.binewatchers;


import android.content.SharedPreferences;

/**
 * 
 * @author Mike
 *
 * Singleton class mit Gettern und Settern für einzelne Preferences
 */
public class PreferenceProvider {
	
	// preferences Identifiers
	public static final String USED_POINTS = "UsedPoints";
	
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
	
	public void setUsedPoints( float usedPoints )
	{
		SharedPreferences.Editor edit = mPreferences.edit();
		edit.putFloat(USED_POINTS, usedPoints);
		edit.commit();
	}

	public float getUsedPoints( )
	{
		float defaultValue = 0.0f;
		return mPreferences.getFloat(USED_POINTS, defaultValue);
	}
	
	

}

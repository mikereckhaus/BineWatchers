package com.example.binewatchers;

import android.widget.EditText;

public class Converter {

	public static Double editTextToDouble( EditText editText)
	{
		Double dailyPoints = 0.0;
		if ( editText != null )
		{
			if (!editText.getText().toString().isEmpty() ){
				
				dailyPoints = Double.parseDouble(editText.getText().toString().replace(',', '.'));
			}
		}
		return dailyPoints;	
	}
}

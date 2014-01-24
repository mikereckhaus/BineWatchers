package com.example.binewatchers;

import android.widget.EditText;

public class Converter {

	public static Double editTextToDouble( EditText editText)
	{
		Double result = 0.0;
		if ( editText != null )
		{
			if (!editText.getText().toString().isEmpty() ){
				
				result = Double.parseDouble(editText.getText().toString().replace(',', '.'));
			}
		}
		return result;	
	}
	
	public static Integer editTextToInt( EditText editText)
	{
		Integer result = 0;
		if ( editText != null )
		{
			if (!editText.getText().toString().isEmpty() ){
				
				result = Integer.parseInt(editText.getText().toString());
			}
		}
		return result;	
	}
}

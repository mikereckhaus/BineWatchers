package com.example.binewatchers;

import java.text.DateFormat;
import java.text.ParseException;

import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
	
	public static Date stringToDate(String string)
	{
	    DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY); 
	    Date result = new Date();
	   
	    try {
	        result = df.parse(string);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
		return result;
	}
	
	public static double[] doubleArrayWrapperToDoubleArray(Double[] input)
	{
		double [] output = new double[input.length];
		
		for (int i =0; i<input.length; i++ )	
		{
			output[i] = input[i];
		}
		return output;
	}
}

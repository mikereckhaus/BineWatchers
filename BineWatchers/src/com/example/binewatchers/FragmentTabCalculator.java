package com.example.binewatchers;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;

public class FragmentTabCalculator extends SherlockFragment {
	
	EditText editTextFat;
	EditText editTextKCal;
	EditText editTextWeight;
	EditText editTextPoints;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Get the view from fragmenttab1.xml
        View view = inflater.inflate(R.layout.fragmenttab_calculator, container, false);
        
    	editTextFat = (EditText) view.findViewById(R.id.editTextFat);
    	editTextKCal = (EditText) view.findViewById(R.id.editTextKCal);
    	editTextWeight = (EditText) view.findViewById(R.id.editTextWeight);
    	editTextPoints = (EditText) view.findViewById(R.id.editTextPoints);	
    	editTextFat.setText("0.0");
    	editTextKCal.setText("0.0");
    	editTextWeight.setText("100.0");
    	editTextPoints.setText("0.0");
    	
    	TextWatcher calculatorWatcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				Double res = calculatePoints();
				editTextPoints.setText(res.toString());
			}
		};
     
       	editTextFat.addTextChangedListener( calculatorWatcher);
    	editTextKCal.addTextChangedListener( calculatorWatcher);
    	editTextWeight.addTextChangedListener( calculatorWatcher);
    	
        return view;
    }
    
    public void onActivityCreated(Bundle savedInstanceState) { 
        super.onActivityCreated(savedInstanceState);
  }
 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
    
    
    double calculatePoints()
    {
		double fat = 0.0;
		if ( !editTextFat.getText().toString().isEmpty() )
		{
			fat = Double.parseDouble(editTextFat.getText().toString());
		}
		
		double kCal = 0.0;
		if ( !editTextKCal.getText().toString().isEmpty() )
		{
			kCal = Double.parseDouble(editTextKCal.getText().toString());
		}
		
		double weight = 0.0;
		if ( !editTextWeight.getText().toString().isEmpty() )
		{
			weight = Double.parseDouble(editTextWeight.getText().toString());
		}
		
		// (cal / 60 + f / 9) * gramm
		return (( kCal / 60 )  + ( fat / 9 )) / 100 * weight;	
    }
}

package com.example.binewatchers;

import java.text.DecimalFormat;

import com.actionbarsherlock.app.SherlockFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FragmentTabDayCount extends SherlockFragment {

	public static final String USED_POINTS = "UsedPoints";
	
	EditText editTextDailyPoints;
	EditText editTextUsedPoints;
	EditText editTextFreePoints;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Get the view from fragmenttab1.xml
        View view = inflater.inflate(R.layout.fragmenttab_daycount, container, false);
        
        editTextDailyPoints = (EditText) view.findViewById(R.id.editTextDailyPoints);
        editTextUsedPoints = (EditText) view.findViewById(R.id.editTextUsedPoints);
        editTextFreePoints = (EditText) view.findViewById(R.id.editTextFreePoints);

        // init preference provider
        PreferenceProvider.getInstance().setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(getActivity()));
		Float usedPoints =  PreferenceProvider.getInstance().getUsedPoints();//getActivity().getPreferences(Context.MODE_PRIVATE).getFloat(USED_POINTS, defaultValue);
        editTextUsedPoints.setText(usedPoints.toString());
        
        // calc free points when used points are changed
        TextWatcher freePointsWatcher = new TextWatcher() {
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
				setFreePoints();
			}
		};
     
       	editTextUsedPoints.addTextChangedListener( freePointsWatcher );
       	setFreePoints();
       	return view;
    }
    
    /**
     * Calc free points from daily points and used ones and sets the textfield
     */
	private void setFreePoints() {
		Double dailyPoints = Converter.editTextToDouble(editTextDailyPoints);
		Double usedPoints = Converter.editTextToDouble(editTextUsedPoints);
		
		Double res = dailyPoints - usedPoints;		
		editTextFreePoints.setText(String.format("%.2f", res));
	}
    
    public void onActivityCreated(Bundle savedInstanceState) { 
        super.onActivityCreated(savedInstanceState);
  }
 
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        setUserVisibleHint(true);
    }
    
    @Override
    public void onStop()
    {
    	super.onStop();
    	float usedPoints = Float.parseFloat(editTextUsedPoints.getText().toString());
    	PreferenceProvider.getInstance().setUsedPoints(	usedPoints);
    }
    
    public void consumePoints(double amount)
    {
    	Double newUsedPoints = Converter.editTextToDouble(editTextUsedPoints) + amount;
    	editTextUsedPoints.setText(String.format("%.2f", newUsedPoints));
    }
	
}

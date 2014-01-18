package com.example.binewatchers;

import java.text.DecimalFormat;

import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class FragmentTabDayCount extends SherlockFragment {

	public static final String USED_POINTS = "UsedPoints";
	
	EditText editTextDailyPoints;
	EditText editTextUsedPoints;
	EditText editTextFreePoints;
	
	Button buttonReset;
	Button buttonAddPoints;
	
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
       		
       	buttonReset = (Button)view.findViewById(R.id.buttonReset);
       	buttonReset.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		        builder.setMessage("Sollen die verbrauchten Punkte wirklich auf 0 zurückgesetzt werden?")
		               .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	   editTextUsedPoints.setText(Double.toString(0.0));
		                   }
		               })
		               .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                       // User cancelled the dialog
		                   }
		               });
		        builder.show();
			}
		});
       	
    	buttonAddPoints = (Button)view.findViewById(R.id.buttonAddPoints);
    	buttonAddPoints.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				LayoutInflater inflater = getActivity().getLayoutInflater();
				final View setPointsView = inflater.inflate(R.layout.set_points_dialog, null);
				builder.setView(setPointsView);
		
				builder.setMessage("Wie viele Punkte sollen abgezogen werden?");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	   EditText editTextPointsToAdd = (EditText)setPointsView.findViewById(R.id.pointsToAdd);
		                	   Double pointsToAdd = Converter.editTextToDouble(editTextPointsToAdd);
		                	   consumePoints(pointsToAdd);
		                   }
		               });
				
				final AlertDialog dialog = builder.create();
				
				// add keyboard on focus
				//dialog.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
				dialog.show();
			}
		});
       	
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
    	Double usedPoints = Converter.editTextToDouble(editTextUsedPoints);
     	PreferenceProvider.getInstance().setUsedPoints(	usedPoints.floatValue() );
    }
    
    public void consumePoints(double amount)
    {
    	Double newUsedPoints = Converter.editTextToDouble(editTextUsedPoints) + amount;
    	editTextUsedPoints.setText(String.format("%.2f", newUsedPoints));
    	
    	TableLayout consumedTable = (TableLayout) getActivity().findViewById(R.id.tableLayoutConsumedPoints);
    	 
    	TableRow row = new TableRow(getActivity());
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);
        
        
        TextView tv = new TextView(getActivity());
        tv.setText("Blabli");
        row.addView(tv);
        
        consumedTable.addView(row);
    }
}

package com.example.binewatchers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Matrix.ScaleToFit;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class FragmentTabDayCount extends SherlockFragment {

	DayCount dayCount = new DayCount();
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
		                	   // clear Table
		                	   dayCount.reset();
		                	   TableLayout consumedTable = (TableLayout) getActivity().findViewById(R.id.tableLayoutConsumedPoints); 
		                	   recreateTable(consumedTable);
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
    	
    	dayCount.restore();
    	editTextUsedPoints.setText(dayCount.getUsedPoints().toString());
        
       	recreateTable((TableLayout) view.findViewById(R.id.tableLayoutConsumedPoints));
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
    	dayCount.persist();
    	Double usedPoints = Converter.editTextToDouble(editTextUsedPoints);
    }
    
    public void consumePoints(final double amount)
    {
    	// add entry in data
    	SimpleDateFormat simpleFormat = new SimpleDateFormat("hh::mm::ss");
    	Date date = new Date();
    	
    	dayCount.addEntry(simpleFormat.format(date), amount);
    	editTextUsedPoints.setText(String.format("%.2f", dayCount.getUsedPoints()));
     	
    	recreateTable( (TableLayout) getActivity().findViewById(R.id.tableLayoutConsumedPoints));
     }
    
    public void recreateTable(final TableLayout consumedTable)
    {	    	
    	
    	for (int i = 0; i <  consumedTable.getChildCount()-1; i++)
    	{
    		consumedTable.removeViewAt(i+1);
    	}
    	
    	HashMap<String, Double> entries = dayCount.getMap();
    	for (Map.Entry<String, Double> entry : entries.entrySet()) {
    		final String key = entry.getKey();
		    final Double value = entry.getValue();
 
	    	//Add a rows to the table
	    	final TableRow row = new TableRow(getActivity());
	    	consumedTable.setStretchAllColumns(true);  
	    	consumedTable.setShrinkAllColumns(true);  
	    	
	        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
	        row.setLayoutParams(lp);
	        
	        // time
	        final TextView time = new TextView(getActivity());
	        time.setText(key);
	        row.addView(time);
	        
	        // points
	        final TextView points = new TextView(getActivity());
	        points.setText(String.format("%.2f", value));
	        row.addView(points);
	        
	        // delete
	        ImageView delete = new ImageView(getActivity());
	        delete.setBackgroundResource(android.R.drawable.ic_delete);
	        delete.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
	        delete.setOnClickListener(new View.OnClickListener() {
				
	        	// really delete dialog?
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			        builder.setMessage("Soll der Eintrag: \n " + key + "\t" + String.format("%.2f", value) + " Punkte" + "\n wirklich entfernt werden?")
			               .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
			                   // on ok delete
			            	   public void onClick(DialogInterface dialog, int id) {
			   					dayCount.removeEntry(time.getText().toString());
			   					consumedTable.removeView(row);
			   			    	editTextUsedPoints.setText(String.format("%.2f", value));
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
	    	row.addView(delete);
	        
	        consumedTable.addView(row);
		    	    
		}
    }
    
}

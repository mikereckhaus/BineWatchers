package com.example.binewatchers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class FragmentTabDayCount extends SherlockFragment {

	PointsPerDay dayCount = new PointsPerDay();
	public static final String USED_POINTS = "UsedPoints";
	
	EditText editTextDailyPoints;
	EditText editTextUsedPoints;
	EditText editTextFreePoints;
	
	Button buttonReset;
	Button buttonAddPoints;

	public FragmentTabDayCount()
	{
		
	}
	
	/*
	 * When points per day changed, store it persistently 
	 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	dayCount.restore();
    	
    	// Get the view from the layout
        View view = inflater.inflate(R.layout.fragmenttab_daycount, container, false);
        
        editTextDailyPoints = (EditText) view.findViewById(R.id.editTextDailyPoints);
        editTextDailyPoints.addTextChangedListener(new TextWatcher() {
			
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
				PreferenceProvider.getInstance().setDailyPoints(Converter.editTextToInt(editTextDailyPoints));
			}
		});
        
        /**
         * Restore daily points on creation
         */
        Integer dailyPoints = PreferenceProvider.getInstance().getDailyPoints();
        if ( dailyPoints != 0 )
        {
        	editTextDailyPoints.setText(dailyPoints.toString());
        }
        
        editTextUsedPoints = (EditText) view.findViewById(R.id.editTextUsedPoints);
        editTextFreePoints = (EditText) view.findViewById(R.id.editTextFreePoints);

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
		
				builder.setMessage("Wie viele Punkte sollen zur Tagessumme hinzugefügt werden?");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	   EditText editTextPointsToAdd = (EditText)setPointsView.findViewById(R.id.pointsToAdd);
		                	   Double pointsToAdd = Converter.editTextToDouble(editTextPointsToAdd);
		                	   consumePoints(pointsToAdd);
		                   }
		               });
				
				final AlertDialog dialog = builder.create();
				
				final EditText editTextPointsToAdd = (EditText)setPointsView.findViewById(R.id.pointsToAdd);
				editTextPointsToAdd.setOnKeyListener(new View.OnKeyListener() {
					@Override
					public boolean onKey(View view, int keyCode, KeyEvent event) {
						if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
								   keyCode == EditorInfo.IME_ACTION_DONE ||
								   event.getAction() == KeyEvent.ACTION_DOWN &&
								   event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
						
							return true;
						}
						return false;
					}
				});
				
				
				dialog.setOnShowListener(new DialogInterface.OnShowListener() {
					
					@Override
					public void onShow(DialogInterface dialog) {
		
						InputMethodManager imm = (InputMethodManager)editTextPointsToAdd.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		                imm.showSoftInput(editTextPointsToAdd, 0);   
					}
				});
								
				dialog.show();
				
			}
		});
    	
    	editTextUsedPoints.setText(String.format("%.2f", dayCount.getUsedPoints()));
        
       	return view;
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
       	recreateTable((TableLayout) getView().findViewById(R.id.tableLayoutConsumedPoints));
    	
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
    }
    
    public void consumePoints(final double amount)
    {
    	// add entry in data
    	SimpleDateFormat simpleFormat = new SimpleDateFormat("HH::mm::ss", Locale.GERMANY);
    	Date date = new Date();
    	
    	dayCount.addEntry(simpleFormat.format(date), amount);
    	editTextUsedPoints.setText(String.format("%.2f", dayCount.getUsedPoints()));
     	
    	recreateTable( (TableLayout) getActivity().findViewById(R.id.tableLayoutConsumedPoints));
     }
    
    public void recreateTable(final TableLayout consumedTable)
    {	    	
    	consumedTable.removeViewsInLayout(1, consumedTable.getChildCount()-1);
    	
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
			   			    	editTextUsedPoints.setText(String.format("%.2f", dayCount.getUsedPoints()));
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
    	consumedTable.invalidate();
    }
}

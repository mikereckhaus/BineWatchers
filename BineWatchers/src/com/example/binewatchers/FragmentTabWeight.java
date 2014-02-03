package com.example.binewatchers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class FragmentTabWeight extends SherlockFragment {
	
	// Data
	WeightHistory weightHistory;
	
	// GUI
	private Button buttonAddWeight;
	private EditText editTextDate;
	private EditText editTextWeight;
	
	// Graph
	private GraphicalView mChart;
	
	XYMultipleSeriesRenderer mRenderer;
	XYMultipleSeriesDataset mDataset;
	TimeSeries mSeries;
	
	public FragmentTabWeight()
	{
		weightHistory = new WeightHistory();
		weightHistory.restore();
		    
	}
	
	public void onResume() {
		super.onResume();
		recreateTable((TableLayout) getActivity().findViewById(R.id.tableLayoutWeight));
		createDiagram((LinearLayout) getView().findViewById(R.id.chart));
		updateChartData();
//		mChart.repaint();
	}
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	// Get the view from fragmenttab1.xml
        View view = inflater.inflate(R.layout.fragmenttab_weight, container, false);
             
		// setzte default date auf heute
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		editTextDate = (EditText)view.findViewById(R.id.editTextDate);
		editTextDate.setText("" + day + "." + (month +  1) + "." + year );
		
		// wenn edit text clicked,  zeige datepicker
		editTextDate .setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
					
					// store date in editText
				    @Override
				    public void onDateSet(DatePicker view, int year, int monthOfYear,
				            int dayOfMonth) {
				    	editTextDate.setText("" + dayOfMonth + "." + ( monthOfYear + 1 ) + "." + year );		    	
				    }
				};

				final Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				
	            new DatePickerDialog(getActivity(), date, year, month, day).show();
			}
		});
		
		editTextWeight = (EditText)view.findViewById(R.id.editTextWeight);				
		
		buttonAddWeight = (Button)view.findViewById(R.id.buttonAddWeight);
		buttonAddWeight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Date date = Converter.stringToDate(editTextDate.getText().toString());
				weightHistory.addEntry(date, Converter.editTextToDouble(editTextWeight));
				recreateTable((TableLayout) getActivity().findViewById(R.id.tableLayoutWeight));
				updateChartData();
			}
		});
		
		return view;
    }

    void createDiagram(LinearLayout layout)
    {
    	mDataset = new XYMultipleSeriesDataset();    	
        
		//mSeries = new TimeSeries("Weight Diagram");
    	mSeries = new TimeSeries("");
		mDataset.addSeries(mSeries);

		updateChartData();
		int[] colors = new int[] { Color.GREEN };
		PointStyle[] styles = new PointStyle[] { PointStyle.POINT };

		    mRenderer = new XYMultipleSeriesRenderer();
		    mRenderer.setAxisTitleTextSize(10);
		    mRenderer.setChartTitleTextSize(10);
		    mRenderer.setLabelsTextSize(10);
		    mRenderer.setLegendTextSize(10);
		    mRenderer.setPointSize(8f);
		    mRenderer.setMargins(new int[] { 15, 25, 10, 15 });
		    int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			    
			// fill crashes on Bines phone
			/*FillOutsideLine fill = new FillOutsideLine(FillOutsideLine.Type.BOUNDS_ABOVE);
			fill.setColor(Color.RED);
			r.addFillOutsideLine(fill);		*/	    
			mRenderer.addSeriesRenderer(r);
		}
		  		    
		mRenderer.setChartTitle("");
		//  mRenderer.setXTitle("date");
		mRenderer.setYTitle("weight / Kg");

		mRenderer.setXAxisMin(weightHistory.getFirstDate().getTime());
		mRenderer.setXAxisMax(weightHistory.getLastDate().getTime());
		mRenderer.setYAxisMin(60);
		mRenderer.setYAxisMax(100);
		mRenderer.setAxesColor(Color.GRAY);
		mRenderer.setLabelsColor(Color.LTGRAY);    
		    
		mChart = ChartFactory.getTimeChartView(getActivity(), mDataset,
		    	//mRenderer, "MMM yyyy");
		    	mRenderer, "MMM");
		layout.addView(mChart); 
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

	public void recreateTable(final TableLayout consumedTable)
    {	    	
    	consumedTable.removeViewsInLayout(1, consumedTable.getChildCount()-1);
    	    	
    	for (Map.Entry<Date, Double> entry : weightHistory.getMap().entrySet()) {
    		final Date key = entry.getKey();
		    final Double value = entry.getValue();
 
	    	//Add a rows to the table
	    	final TableRow row = new TableRow(getActivity());
	    	consumedTable.setStretchAllColumns(true);  
	    	consumedTable.setShrinkAllColumns(true);  
	    	
	        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
	        row.setLayoutParams(lp);
	        
	        // time
	        final TextView time = new TextView(getActivity());
	        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
	        time.setText(df.format(key));
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
					SimpleDateFormat d = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
			        builder.setMessage("Soll der Eintrag: \n " + d.format(key) + "\t" + String.format("%.2f", value) + "Kg" + "\n wirklich entfernt werden?")
			               .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
			                   // on ok delete
			            	   public void onClick(DialogInterface dialog, int id) {
			            		Date date = Converter.stringToDate(time.getText().toString()); 
			   					weightHistory.removeEntry(date);
			   					consumedTable.removeView(row);
			   					updateChartData();
			   					mChart.repaint();
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
	
	void updateChartData()
	{
	    Date[] dateValues = weightHistory.getMap().keySet().toArray(new Date[weightHistory.getMap().keySet().size()]);
	
	    Double[] weightValues = weightHistory.getMap().values().toArray(new Double[weightHistory.getMap().values().size()]);  
	    double[] converted = Converter.doubleArrayWrapperToDoubleArray(weightValues);
	    
		mSeries = (TimeSeries) (mDataset.getSeries())[0];
		mSeries.clear();
		int seriesLength = dateValues.length;
		for (int k = 0; k < seriesLength; k++) {
			mSeries.add(dateValues[k], converted[k]);
		}
	}
}

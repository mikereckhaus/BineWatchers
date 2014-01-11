package com.example.binewatchers;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	Button buttonCalculate;
	
	EditText editTextFat;
	EditText editTextKCal;
	EditText editTextWeight;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        editTextFat = (EditText) findViewById(R.id.editTextFat);
		editTextKCal = (EditText) findViewById(R.id.editTextKCal);
		editTextWeight = (EditText) findViewById(R.id.editTextWeight);
		editTextFat.setText("0.0");
		editTextKCal.setText("0.0");
		editTextWeight.setText("100.0");
        
        buttonCalculate = (Button) findViewById(R.id.buttonCalculate);
        
		buttonCalculate.setOnClickListener(new OnClickListener() {
	       	 
			@Override
			public void onClick(View arg0) {
 
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
				
				EditText editTextResult = (EditText) findViewById(R.id.editTextPoints);
				// (cal / 60 + f / 9) * gramm
				Double res = (( kCal / 60 )  + ( fat / 9 )) / 100 * weight;
				editTextResult.setText(res.toString());
			}
 
		});
    }


    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}

package com.example.binewatchers;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class FragmentTabCalculator extends SherlockFragment {

	EditText editTextFat;
	EditText editTextKCal;
	EditText editTextWeight;
	EditText editTextPoints;
	Button buttonEat;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab1.xml
		View view = inflater.inflate(R.layout.fragmenttab_calculator,
				container, false);

		View.OnClickListener clearOnClick = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((EditText) v).setText("");
			}
		};
		
		editTextFat = (EditText) view.findViewById(R.id.editTextFat);
		editTextFat.setOnClickListener(clearOnClick);
		editTextKCal = (EditText) view.findViewById(R.id.editTextKCal);
		editTextKCal.setOnClickListener(clearOnClick);
		editTextWeight = (EditText) view.findViewById(R.id.editTextWeight);
		editTextWeight.setOnClickListener(clearOnClick);
		editTextPoints = (EditText) view.findViewById(R.id.editTextPoints);
		editTextFat.setText("0");
		editTextKCal.setText("0");
		editTextWeight.setText("100");
		editTextPoints.setText("0.0");

		TextWatcher calculatorWatcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
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
				editTextPoints.setText(String.format("%.2f", res));
			}
		};

		editTextFat.addTextChangedListener(calculatorWatcher);
		editTextKCal.addTextChangedListener(calculatorWatcher);
		editTextWeight.addTextChangedListener(calculatorWatcher);

		buttonEat = (Button)view.findViewById(R.id.buttonEat);
		buttonEat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IPointsConsumerListener act = (IPointsConsumerListener)getActivity();
				act.consumePoints( Converter.editTextToDouble(editTextPoints) );
			}
		});
		
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

	double calculatePoints() {
		double fat = Converter.editTextToDouble(editTextFat);
		double kCal = Converter.editTextToDouble(editTextKCal);
		double weight = Converter.editTextToDouble(editTextWeight);
		
		// (cal / 60 + f / 9) * gramm
		return ((kCal / 60) + (fat / 9)) / 100 * weight;
	}
}

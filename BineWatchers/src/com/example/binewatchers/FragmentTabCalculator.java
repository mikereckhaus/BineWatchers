package com.example.binewatchers;

import android.content.Context;
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

		View.OnFocusChangeListener clearOnClick = new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if ( hasFocus )
				{
					((EditText) v).setText("");
				}
			};
		};
		
		
		editTextFat = (EditText) view.findViewById(R.id.editTextFat);
		editTextFat.setOnFocusChangeListener(clearOnClick);
		editTextKCal = (EditText) view.findViewById(R.id.editTextKCal);
		editTextKCal.setOnFocusChangeListener(clearOnClick);
		editTextWeight = (EditText) view.findViewById(R.id.editTextWeight);
		editTextWeight.setOnFocusChangeListener(clearOnClick);
		editTextPoints = (EditText) view.findViewById(R.id.editTextPoints);
		editTextWeight.setText("100");
		
		editTextWeight.setOnKeyListener(new View.OnKeyListener() {
			
			/**
			 * When done, hide the keyboard to see the auto calculated
			 * result on small screens
			 */
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (keyCode == EditorInfo.IME_ACTION_SEARCH ||
						   keyCode == EditorInfo.IME_ACTION_DONE ||
						   event.getAction() == KeyEvent.ACTION_DOWN &&
						   event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
				
					InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	                imm.hideSoftInputFromWindow(editTextWeight.getWindowToken(), 0);   
					return true;
				}
				return false;
			}
		});
		
		/**
		 * Automatically calculate points when any value field changes
		 */
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

		/**
		 * Whenn eaten, send to other fragment via the activity
		 */
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

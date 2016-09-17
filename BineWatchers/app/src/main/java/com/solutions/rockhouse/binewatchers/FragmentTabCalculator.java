package com.solutions.rockhouse.binewatchers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentTabCalculator extends Fragment implements NumberPicker.OnValueChangeListener {

	final String KCAL_BUNDLE_ID = "KCAL_BUNDLE_ID";
	final String GRAM_BUNDLE_ID = "GRAM_BUNDLE_ID";
	final String FAT_BUNDLE_ID = "FAT_BUNDLE_ID";

	// step size for pickers
	private int gramStepsize = 50;
	private int kCalStepsize = 1;
	private int fatStepsize = 1;


	Button buttonEat;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab1.xml
		View view = inflater.inflate(R.layout.fragmenttab_calculator,
				container, false);

		/**
		 * Whenn eaten, send to other fragment via the activity
		 */
		buttonEat = (Button)view.findViewById(R.id.buttonEat);
		buttonEat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				IPointsConsumerListener act = (IPointsConsumerListener)getActivity();
				act.consumePoints( Converter.textViewToDouble((TextView) getView().findViewById(R.id.textPoints)) );
			}
		});


		NumberPicker pickerFat = (NumberPicker) view.findViewById(R.id.pickerFat);
		configurePicker( pickerFat, fatStepsize, 100, 3);

		NumberPicker pickerGram = (NumberPicker) view.findViewById(R.id.pickerGram);
		configurePicker( pickerGram, gramStepsize, 20, 3);

		NumberPicker pickerKCal = (NumberPicker) view.findViewById(R.id.pickerKCal);
		configurePicker( pickerKCal, kCalStepsize, 600, 100);

		return view;
	}

	/**
	 * configure a picker
	 * @param picker - view
	 * @param stepsize -
	 * @param maxSteps - number of entries in the picker
	 * @param initialStep - value after creation
     */
	private void configurePicker(NumberPicker picker, int stepsize, int maxSteps, int initialStep) {
		picker.setMaxValue(maxSteps-1);
		picker.setMinValue(0);

		// set standard value if valid
		if ( initialStep < maxSteps ) {
			picker.setValue(initialStep);
		}
		else{
			picker.setValue(maxSteps-1);
		}

		picker.setWrapSelectorWheel(true);
		picker.setOnValueChangedListener(this);

		// set the values
		ArrayList<String> values = new ArrayList<>();
		for (int i = picker.getMinValue(); i <= picker.getMaxValue(); i++) {
			values.add(String.valueOf((i* stepsize)));
		}
		String[] valArray = values.toArray(new String[values.size()]);
		picker.setDisplayedValues(valArray);

	}


	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// restore state
		if ( savedInstanceState != null ) {
			if (savedInstanceState.containsKey(GRAM_BUNDLE_ID)) {
				((NumberPicker) getView().findViewById(R.id.pickerGram)).setValue(savedInstanceState.getInt(GRAM_BUNDLE_ID));
			}
			if (savedInstanceState.containsKey(FAT_BUNDLE_ID)) {
				((NumberPicker) getView().findViewById(R.id.pickerFat)).setValue(savedInstanceState.getInt(FAT_BUNDLE_ID));
			}
			if (savedInstanceState.containsKey(KCAL_BUNDLE_ID)) {
				((NumberPicker) getView().findViewById(R.id.pickerKCal)).setValue(savedInstanceState.getInt(KCAL_BUNDLE_ID));
			}
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// save state
		outState.putInt(GRAM_BUNDLE_ID, ((NumberPicker)getView().findViewById(R.id.pickerGram)).getValue());
		outState.putInt(FAT_BUNDLE_ID, ((NumberPicker)getView().findViewById(R.id.pickerFat)).getValue());
		outState.putInt(KCAL_BUNDLE_ID, ((NumberPicker)getView().findViewById(R.id.pickerKCal)).getValue());
		setUserVisibleHint(true);
	}

	@Override
	public void onResume(){
		super.onResume();
		updatePoints();
	}

	double calculatePoints() {
		double fat = ((NumberPicker)getView().findViewById(R.id.pickerFat)).getValue() * fatStepsize;
		double kCal = ((NumberPicker)getView().findViewById(R.id.pickerKCal)).getValue() * kCalStepsize;
		double gram = ((NumberPicker)getView().findViewById(R.id.pickerGram)).getValue() * gramStepsize;

		double  result = 0.0;
		if ( gram > 0 ) {
			// (cal / 60 + f / 9) * gramm
			result = ((kCal / 60) + (fat / 9)) / 100 * gram;
		}
		return result;
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		updatePoints();
	}

	private void updatePoints() {
		Double res = calculatePoints();
		((TextView)getView().findViewById(R.id.textPoints)).setText(String.format("%.2f", res));
	}
}

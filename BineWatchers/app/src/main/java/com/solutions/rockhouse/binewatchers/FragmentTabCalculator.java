package com.solutions.rockhouse.binewatchers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentTabCalculator extends Fragment implements NumberPicker.OnValueChangeListener, TextWatcher {

	final String BUNDLE_KCAL = "BUNDLE_KCAL";
	final String BUNDLE_GRAM = "BUNDLE_GRAM";
	final String BUNDLE_FAT = "BUNDLE_FAT";

	private Unbinder unbinder;

	@BindView(R.id.edit_fat) EditText editFat;
	@BindView(R.id.edit_gram) EditText editGram;
	@BindView(R.id.edit_kcal) EditText editKcal;

	@BindView(R.id.edit_points) EditText editPoints;
	@BindView(R.id.image_check)	ImageView imageCheck;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Get the view from fragmenttab1.xml
		final View view = inflater.inflate(R.layout.fragmenttab_calculator,
				container, false);

		unbinder = ButterKnife.bind(this, view);

		editFat.addTextChangedListener(this);
		editGram.addTextChangedListener(this);
		editKcal.addTextChangedListener(this);
		editPoints.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				imageCheck.setVisibility(View.GONE);
			}
		});

		return view;
	}

	/**
	 * Whenn eaten, send to other fragment via the activity
	 */
	@OnClick(R.id.action_add)
	public void consumePoints(View view){
		IPointsConsumerListener act = (IPointsConsumerListener)getActivity();
		act.consumePoints( Converter.textViewToDouble( editPoints ) );

		// indicate by making points green
		imageCheck.setVisibility(View.VISIBLE);
	}

	/**
	 * configure a picker
	 * @param picker - view
	 * @param stepsize -
	 * @param maxSteps - number of entries in the picker
	 * @param initialStep - value after creation
     */
	/*private void configurePicker(NumberPicker picker, int stepsize, int maxSteps, int initialStep) {
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

	}*/


	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// restore state
		if ( savedInstanceState != null ) {
			if (savedInstanceState.containsKey(BUNDLE_FAT)) {
				editFat.setText(savedInstanceState.getString(BUNDLE_FAT));
			}
			if (savedInstanceState.containsKey(BUNDLE_GRAM)) {
				editGram.setText(savedInstanceState.getString(BUNDLE_GRAM));
			}
			if (savedInstanceState.containsKey(BUNDLE_KCAL)) {
				editKcal.setText(savedInstanceState.getString(BUNDLE_KCAL));
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// save state
		outState.putString(BUNDLE_FAT, editFat.getText().toString());
		outState.putString(BUNDLE_GRAM, editGram.getText().toString());
		outState.putString(BUNDLE_KCAL, editKcal.getText().toString());
		setUserVisibleHint(true);
	}

	@Override
	public void onResume(){
		super.onResume();
		updatePoints();
	}

	double calculatePoints() {
		double fat = 1.0;
		double gram = 100.0;
		double kCal = 100.0;

		try {
			fat = Double.valueOf(editFat.getText().toString());
			gram = Double.valueOf(editGram.getText().toString());
			kCal = Double.valueOf(editKcal.getText().toString());
		}
		catch (NumberFormatException e){
			return 0.0;
		}

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
		imageCheck.setVisibility(View.GONE);
	}

	private void updatePoints() {
		Double res = calculatePoints();
		editPoints.setText(String.format("%.2f", res));
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		updatePoints();
		imageCheck.setVisibility(View.GONE);
	}

	@Override public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}
}

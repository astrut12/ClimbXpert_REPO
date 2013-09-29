package com.example.climbxpert;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FindNearPOIActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_near_poi);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_near_poi, menu);
		return true;
	}

}

package com.example.climbxpert;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class RouteInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_info);
		

		Intent intent = getIntent();
		
		
		String info = intent.getStringExtra("info");
		String rank = intent.getStringExtra("rank");
		
		TextView tvInfo = (TextView) findViewById(R.id.route_info);
		TextView tvRank = (TextView) findViewById(R.id.route_rank);
		
		tvInfo.setText("Info:" + info);
		tvRank.setText("Level:" + rank);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.route_info, menu);
		return true;
	}

}

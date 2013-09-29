package com.example.climbxpert;

import com.parse.Parse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddNewRouteActivity extends Activity {
 
	private Button route_btnCancel, route_btnOK;
	private EditText name;
	private EditText info;
	private EditText tilt;
	private EditText imgId;
	private EditText azimuth;
	private Spinner route_rank;
	private int pid;
	
	public void onCreate(Bundle unused) { 
		super.onCreate(unused); 
		
		Intent intent = getIntent();
		
		pid = intent.getIntExtra("pid",-1);
		setContentView(R.layout.add_route_layout);
		route_btnOK = (Button)findViewById(R.id.route_btnOK);
		route_btnCancel = (Button)findViewById(R.id.route_btnCancel);
		imgId = (EditText)findViewById(R.id.route_image);
		name = (EditText)findViewById(R.id.route_name);
		info = (EditText)findViewById(R.id.route_info);
		azimuth = (EditText)findViewById(R.id.route_azimuth);
		tilt = (EditText)findViewById(R.id.route_tilt);
		route_rank = (Spinner)findViewById(R.id.route_rank_spinner);
		
		
        Parse.initialize(this, "Mldts0UdI2e7ndjgYIzcAdFiIRKYRiOhYPrQWzqx", "nyRer8Q1FzdfD2oHp0IekZX4DLAeXXcXciVDgPAf");
		
		
		route_btnOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(name.getText().toString().trim().equals("")) {
					setResult(RESULT_CANCELED); 
					finish();
				}
			
				Intent result = new Intent();	
				result.putExtra("route_name",name.getText().toString());
				result.putExtra("route_info",info.getText().toString());
				result.putExtra("route_image",Integer.parseInt(imgId.getText().toString()));
				result.putExtra("route_tilt",Double.parseDouble(tilt.getText().toString()));
				result.putExtra("route_azimuth",Double.parseDouble(azimuth.getText().toString()));
				result.putExtra("pid", pid);
				result.putExtra("rank", String.valueOf(route_rank.getSelectedItem()));
				setResult(RESULT_OK, result); 
				finish();
			}
		});
				
		route_btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setResult(RESULT_CANCELED); 
				finish();
			}
		});	
			
	} 	
	
	  public void addListenerOnSpinnerItemSelection() {
			route_rank = (Spinner) findViewById(R.id.route_rank_spinner);
			route_rank.setOnItemSelectedListener(new CustomOnItemSelectedListener());
		  }
}

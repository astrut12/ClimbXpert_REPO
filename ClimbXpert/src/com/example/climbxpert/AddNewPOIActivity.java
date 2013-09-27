package com.example.climbxpert;

import com.parse.Parse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewPOIActivity extends Activity {
 
	private Button btnCancel, btnOK, btnBMP;
	private EditText name;
	private EditText info;
	private EditText lon;
	private EditText lat;
	private EditText azimuth;
	
	public void onCreate(Bundle unused) { 
		super.onCreate(unused); 
		setContentView(R.layout.poi_info_layout);
		btnOK = (Button)findViewById(R.id.btnOK);
		btnCancel = (Button)findViewById(R.id.btnCancel);
		btnBMP = (Button)findViewById(R.id.btnImg);
		name = (EditText)findViewById(R.id.name);
		info = (EditText)findViewById(R.id.info);
		lon = (EditText)findViewById(R.id.lon);
		lat = (EditText)findViewById(R.id.lat);
		azimuth = (EditText)findViewById(R.id.azimuth);
		
        Parse.initialize(this, "Mldts0UdI2e7ndjgYIzcAdFiIRKYRiOhYPrQWzqx", "nyRer8Q1FzdfD2oHp0IekZX4DLAeXXcXciVDgPAf");
		
		
		btnOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(name.getText().toString().trim().equals("")) {
					setResult(RESULT_CANCELED); 
					finish();
				}
				 
				Intent result = new Intent();
				String str = String.valueOf(ClimbXpertData.pid);
				Log.d("str",str);
				result.putExtra("pid",ClimbXpertData.pid);
				result.putExtra("name",name.getText().toString());
				result.putExtra("info",info.getText().toString());
				result.putExtra("lon",Double.parseDouble(lon.getText().toString()));
				result.putExtra("lat",Double.parseDouble(lat.getText().toString()));
				result.putExtra("azimuth",Double.parseDouble(azimuth.getText().toString()));
				setResult(RESULT_OK, result); 
				ClimbXpertData.pid++;
				finish();
			}
		});
		
		btnBMP.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 
				//TODO: finish
			}
		});
		
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setResult(RESULT_CANCELED); 
				finish();
			}
		});	
			
	} 	
}
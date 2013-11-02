package com.example.climbxpert;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.os.Bundle;

public class SearchActivityNew extends Activity {

	// The map instance.
	private GoogleMap mMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		LatLng sydney = new LatLng(-33.867, 151.206);
		
		mMap.setMyLocationEnabled(true);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
		
		mMap.addMarker(new MarkerOptions()
				.title("Sydney")
				.snippet("adfadfdf")
				.position(sydney));
	}

//	@Override
//	protected void onStart() {
//		// TODO Auto-generated method stub
//		super.onStart();
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//	}
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//	}
//
//	@Override
//	protected void onStop() {
//		// TODO Auto-generated method stub
//		super.onStop();
//	}
//
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//	}
	
}

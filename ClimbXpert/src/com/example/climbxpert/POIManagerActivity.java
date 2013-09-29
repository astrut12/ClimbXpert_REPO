package com.example.climbxpert;

import com.example.climbxpert.POI.POI;
import com.google.android.gms.maps.model.LatLng;
import com.parse.Parse;
import com.parse.ParseObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class POIManagerActivity extends Activity {
	
	private final int ADD_POI_ITEM_RESULT = 1;
	private final int ADD_ROUTE_ITEM_RESULT = 2;
	private ListView listView; 
	//private ArrayList<POI> arrayList;
	private POIDAL _dal;
	private RBAdapter POIAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi_manager);
		_dal = new POIDAL(this);
		//ClimbXpertData.POIList = intent.getParcelableArrayListExtra("poi");
		//arrayList = _dal.all();
	    Parse.initialize(this, "Mldts0UdI2e7ndjgYIzcAdFiIRKYRiOhYPrQWzqx", "nyRer8Q1FzdfD2oHp0IekZX4DLAeXXcXciVDgPAf"); 

		listView = (ListView)findViewById(R.id.POIItems); 
		POIAdapter = new RBAdapter(this,android.R.layout.simple_list_item_1,ClimbXpertData.POIList);
		registerForContextMenu(listView);
		
		
		listView.setAdapter(POIAdapter);
		
		
		
	}

	protected void onActivityResult(int reqCode, int resCode, Intent data) { 
			switch (reqCode) { 
				case ADD_POI_ITEM_RESULT: { 
					switch (resCode) {
					case (RESULT_CANCELED) : {
						return;
					}
					case (RESULT_OK) : {
						LatLng ll = new LatLng(data.getDoubleExtra("lon", -1.0), data.getDoubleExtra("lat", -1.0));
						LatLng stlll = new LatLng(data.getDoubleExtra("STLlon", -1.0), data.getDoubleExtra("STLlat", -1.0));
						_dal.insert(new POI(data.getIntExtra("pid", -1), data.getStringExtra("name"),
								data.getStringExtra("info"),data.getIntExtra("imgId", -1),ll,stlll,null)); 
						//_dal.insert(new POI((String)data.getStringExtra("title"),(Date)data.getSerializableExtra("dueDate")));
						POIAdapter.add(new POI(data.getIntExtra("pid", -1), data.getStringExtra("name"),
								data.getStringExtra("info"),data.getIntExtra("imgId", -1),ll,stlll,null));
						Log.d("POIManager","inserted intems");
					}
				}
					break;
			}
				case ADD_ROUTE_ITEM_RESULT: {
					switch (resCode) {
					case (RESULT_CANCELED) : {
						return;
					}
					case (RESULT_OK) : {
						ParseObject parseObject = new ParseObject("Route");
						parseObject.put("name", data.getStringExtra("route_name"));
						parseObject.put("info", data.getStringExtra("route_info"));
						parseObject.put("imgId", data.getIntExtra("route_image", -1));
						parseObject.put("azimuth", data.getDoubleExtra("route_azimuth",-1));
						parseObject.put("tilt", data.getDoubleExtra("route_tilt",-1));
						parseObject.put("pid", data.getIntExtra("pid", -1));
						parseObject.put("rank", data.getStringExtra("rank"));
						parseObject.saveInBackground();
						Log.d("Route","sent to parse");
						
					}
				}			
				break;
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poi_item_manager, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) { 
		switch (item.getItemId()) {
			case R.id.menuItemAdd : {
				Intent intent = new Intent(this,AddNewPOIActivity.class);  
				startActivityForResult(intent, ADD_POI_ITEM_RESULT); 
				return true;
			}
			default : {
				return false;
			}
		}
	}
	
	public void onCreateContextMenu( ContextMenu menu, View v, ContextMenuInfo info) { 
		super.onCreateContextMenu(menu, v, info); 
		getMenuInflater().inflate(R.menu.poi_manager_context_menu, menu);
		POI item = (POI)POIAdapter.getItem(((AdapterContextMenuInfo)info).position);
		menu.setHeaderTitle(item.name);
	}

	public boolean onContextItemSelected(MenuItem item) { 
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		 switch (item.getItemId()) {
		 	case R.id.menuItemDelete: {
		 		_dal.delete((POI)POIAdapter.getItem((info.position)));
		 		POIAdapter.remove(POIAdapter.getItem((info.position)));
		 		ClimbXpertData.pid--;
		 		return true;
		 	}
		 	case R.id.menuItemAddRoute: {
		 		Intent intent = new Intent(this,AddNewRouteActivity.class);
		 		intent.putExtra("pid",((POI)POIAdapter.getItem(info.position)).pid);
				startActivityForResult(intent, ADD_ROUTE_ITEM_RESULT); 
				return true;
		 	}
		 	default: {
		 		return false;
		 	}
		 }
	}
}
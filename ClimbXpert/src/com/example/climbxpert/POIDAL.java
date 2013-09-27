package com.example.climbxpert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.climbxpert.POI.POI;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;

public class POIDAL {

	private List<POI> _POI_List;
	private Cursor cursor;
	protected SimpleCursorAdapter POIAdapter;
	
	public POIDAL(Context context) {
		
		_POI_List = new ArrayList<POI>();

		String[] from = new String[] { "pid", "name" }; 
		int[] to = new int[] { R.id.txtPOIid, R.id.txtPOIName};  
		 
		 POIAdapter = new SimpleCursorAdapter(context,R.layout.row,cursor,from,to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		 
	}
	
	public boolean insert(POI POIItem) {
		
		ParseObject parseObject = new ParseObject("POI");
		Log.d("POIDAL","in insert");
		try {	
			parseObject.put("pid",String.valueOf(POIItem.pid));
			parseObject.put("name",POIItem.name);
			parseObject.put("info",POIItem.info);
			parseObject.put("lon",POIItem.carNavigation.longitude);
			parseObject.put("STLlon",POIItem.standLocation.longitude);
			parseObject.put("STLlat",POIItem.standLocation.latitude);
			parseObject.put("lat",POIItem.carNavigation.latitude);
			parseObject.put("imgId", POIItem.imageID);
			//TODO: finish implementation
			parseObject.saveInBackground();
			Log.d("POIDAL","sent to parse");
		 }
		 catch(Exception e) {
			 Log.d("Parse","Failed inserting item:" + e.getMessage());
			return false; 
		 }	 
		POIAdapter.notifyDataSetChanged();
		return true;
	 }
	
	
	public boolean update(POI POIItem) {
		//TODO:???
/*		try
		{
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("POI");
			query.whereEqualTo("title", POIItem.getTitle());
			ParseObject parseItem = query.getFirst();
			parseItem.put("due", todoItem.getDueDate().getTime());
			parseItem.saveInBackground();
		}
		catch(Exception e) {
			return false;
		}*/
		return true; 
		 
	}
	 
	public boolean delete(POI POIItem) {
		try
		{
			ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("POI");
			query.whereEqualTo("id", POIItem.pid);
			query.findInBackground(new FindCallback() {
				

				void internalDone(Object arg0, ParseException arg1) {
					// TODO Auto-generated method stub
					
				}
				public void done(List results, ParseException e) {
					if (e != null) {
			        	//error
			        } 
			        else {
			        	if(!results.isEmpty()) {
			        		try {
			        			((ParseObject)results.get(0)).delete();
			        		} 
			        		catch (ParseException e1) {
						
			        		}	
			        	}
			        }
				}
			});
		//	ParseObject parseItem = query.getFirst();
		//	parseItem.delete();
		}
		catch (Exception e)
		{
			Log.d("Parse","Failed deleting item:" + e.getMessage()+ " "+e.getStackTrace().toString());
			return false;
		}
		return true;
	 }
	 
	public List<POI> all() {
		_POI_List = new ArrayList<POI>();
		return _POI_List;
	}

}

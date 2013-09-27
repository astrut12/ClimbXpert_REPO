package com.example.climbxpert;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.example.climbxpert.POI.POI;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RBAdapter extends ArrayAdapter<POI> {
	
	    
	    public RBAdapter(Context context, int resource, List<POI> arrayList) {
	        super(context, resource, arrayList);
	    }
	    
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	LayoutInflater inflater = LayoutInflater.from(getContext()); 
	    	View view = inflater.inflate(R.layout.row, null);
	    	TextView id = (TextView)view.findViewById(R.id.txtPOIid);
	    	TextView name = (TextView)view.findViewById(R.id.txtPOIName);
	    	POI item = (POI)getItem(position);
	    	
	    	//set title
	    	if(item == null){
	    		Log.d("RBAdapter","Item is NULL");
	    	}
	    	else {
		    	id.setText(String.valueOf(item.pid)); 
		    	name.setText(item.name);
	    	}
	/*		else {
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyy",Locale.US);
				String dateString = formatter.format(itemDate);
				name.setText(dateString);
			}
	    	
	    	if((item.getDueDate() != null) && ((item.getDueDate().compareTo(new Date()))<0)) {
	    		id.setTextColor(Color.RED);
	    		name.setTextColor(Color.RED);
	    	}*/
			  return view;
	    }
	  
	}

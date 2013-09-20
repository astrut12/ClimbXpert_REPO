package com.example.climbxpert;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
	//TODO: create a separate class with the activities id's for better references
	//The ID in intent for activity id
	public final static String ACTIVITY_ID = "activityID";
	
	//This activity's id
	public final static String MAIN_ACTIVITY_ID = "main_activity";
    
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public void openSearch(View view)
    {
    	//opening the search window with an indication that it came from the main window 
    	//(to distinguish calls from outer intents or from navigation context)
    	Intent intent = new Intent(this, SearchActivity.class);
    	intent.putExtra(ACTIVITY_ID, MAIN_ACTIVITY_ID);
    	startActivity(intent);
    }
    
    
    public void openNavigate(View view)
    {
    	//TODO: check if there are relevant locations nearby before opening the navigate activity
    	//consider putting the location search logic in the navigation activity (makes it simpler since we need to search for the nearest location in the navigation activity anyway). 
    	
    	//opening the navigate window with an indication that it came from the main window 
    	//(to distinguish calls from outer intents or from navigation context)
    	Intent intent = new Intent(this, NavigateActivity.class);
    	intent.putExtra(ACTIVITY_ID, MAIN_ACTIVITY_ID);
    	startActivity(intent);
    }
    
    public void openCameraView(View view)
    {
    	Intent intent = new Intent(this, CameraViewActivity.class);
    	intent.putExtra(ACTIVITY_ID, MAIN_ACTIVITY_ID);
    	startActivity(intent);
    }
    
    
    public void openTestView(View view)
    {
    	Intent intent = new Intent(this, TestActivity.class);
    	intent.putExtra(ACTIVITY_ID, MAIN_ACTIVITY_ID);
    	startActivity(intent);
    }
    
}

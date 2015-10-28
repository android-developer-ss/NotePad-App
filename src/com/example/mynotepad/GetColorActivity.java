package com.example.mynotepad;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.widget.RadioButton;

public class GetColorActivity extends Activity {
	
	//private String blueColor = "BLUE";
	private String blackColor = "BLACK";
	private String cyanColor = "CYAN";	
	private String dkgrayColor = "DKGRAY";	
	private String grayColor = "GRAY";	
	private String greenColor = "GREEN";	
	private String ltgrayColor = "LTGRAY";	
	private String magentaColor = "MAGENTA";	
	private String redColor = "RED";	
	private String transparentColor = "TRANSPARENT";	
	private String whiteColor = "WHITE";	
	private String yellowColor = "YELLOW";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_color);
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    Intent resultData = new Intent();
	    String color = "Color";
	    
	    resultData.putExtra("token", color);
	    //resultData.putExtra("value", value);
	    
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.red_color:
	            if (checked)
	                resultData.putExtra("value", redColor);
	            break;
	        case R.id.green_color:
	            if (checked)
	                resultData.putExtra("value", greenColor);
	            break;
	        case R.id.black_color:
	            if (checked)
	                resultData.putExtra("value", blackColor);
	            break;
	        case R.id.blue_color:
	            if (checked)
	                resultData.putExtra("value", MainActivity.blueColor);
	            break;
	        case R.id.cyan_color:
	            if (checked)
	                resultData.putExtra("value", cyanColor);
	            break;
	        case R.id.dkgray_color:
	            if (checked)
	                resultData.putExtra("value", dkgrayColor);
	            break;
	        case R.id.ltgray_color:
	            if (checked)
	                resultData.putExtra("value", ltgrayColor);
	            break;
	        case R.id.gray_color:
	            if (checked)
	                resultData.putExtra("value", grayColor);
	            break;
	        case R.id.magenta_color:
	            if (checked)
	                resultData.putExtra("value", magentaColor);
	            break;
	        case R.id.transparent_color:
	            if (checked)
	                resultData.putExtra("value", transparentColor);
	            break;
	        case R.id.white_color:
	            if (checked)
	                resultData.putExtra("value", whiteColor);
	            break;
	        case R.id.yellow_color:
	            if (checked)
	                resultData.putExtra("value", yellowColor);
	            break;
	    }
	    
	    setResult(Activity.RESULT_OK, resultData);
	    finish();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_color, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	

}

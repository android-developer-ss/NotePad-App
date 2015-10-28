package com.example.mynotepad;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static int titleCounter = 0;

    private EditText mTitleText;
    private EditText mBodyText;
    private Long mRowId;    
    static NotesDbAdapter mDbHelper;
    
    private EditText input;	
    final Context context = this;
    
	private int BACKGROUND_COLOR = 1;
	private int TEXT_COLOR = 2;
	private int SHOWRECENTDOCUMENTS = 3;
	
	static String blueColor = "BLUE";
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
        setContentView(R.layout.activity_main);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        
        getOverflowMenu();
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
       //fillData();
        //registerForContextMenu(getListView());
        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.message_body);
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
									: null;
		}

		populateFields();
    }
    
    private void populateFields() {
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchNote(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_TITLE)));
            mBodyText.setText(note.getString(
                    note.getColumnIndexOrThrow(NotesDbAdapter.KEY_BODY)));
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putSerializable(NotesDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        //If user has to entered any Name/Title then just save it
        //using default name.
        if(title.matches("")){
        	title = "Note" + titleCounter++;
        }

        if (mRowId == null) {
            long id = mDbHelper.createNote(title, body);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateNote(mRowId, title, body);
        }
        
        Toast.makeText(this, title+" saved", Toast.LENGTH_SHORT).show();
        
    }
    
    private void getOverflowMenu() {

        try {
           ViewConfiguration config = ViewConfiguration.get(this);
           Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
           if(menuKeyField != null) {
               menuKeyField.setAccessible(true);
               menuKeyField.setBoolean(config, false);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
   }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
       // return true;
        
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                //openSearch();
                return true;
            case R.id.action_settings:
                //openSettings();
                return true;
            case R.id.action_bkgd_color:
            	//startActivity(new Intent(this, ColorPickerActivity.class));
            	//R.id.message_body.setTextColor(Color.DKGRAY);
            	//return true;
            	getBackgroundColor();
            	//EditText editText = (EditText) findViewById(R.id.message_body);
            	//editText.setTextColor(Color.RED);
            	//editText.setBackgroundColor(Color.WHITE);
            	return true;
            case R.id.action_text_color:
            	getTextColor();
            	return true;
            case R.id.action_recent_docs:
            	showRecentDocuments();
            	return true;
            case R.id.action_save:
            	 setResult(RESULT_OK);
                 finish();
                 Intent intent = new Intent(this, MainActivity.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             	 startActivity(intent);
             	//startActivityForResult(intent, MAINACTIVITY);
                 return true;
            case R.id.action_saveas:
            	saveAsFunction(context);
            	//finish();
                //Intent intent2 = new Intent(this, MainActivity.class);
            	//startActivity(intent2);
            	return true;
            case R.id.action_new_note:
            	setResult(RESULT_OK);
            	finish();
                Intent intent1 = new Intent(this, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(intent1);
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    
    public void saveAsFunction(final Context context)
	{
        LayoutInflater layoutInflater = LayoutInflater.from(context);
		View promptView = layoutInflater.inflate(R.layout.dialog_saveas, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(promptView);
		alertDialogBuilder.setCancelable(false);
		input = (EditText) promptView.findViewById(R.id.newName);
		
		
		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() 
		{
			@Override
			public void onClick(DialogInterface dialog,int id)
			{
				String title = input.getText().toString();
				mTitleText.setText(title);
				
				 String body = mBodyText.getText().toString();

			        
		        	long id1 = mDbHelper.createNote(title, body);
		        	if(id1 > 0)
		        	{
		        		mRowId = id1;
		        	}
		        	else
		        	{
		        		Log.e("saveState","failed to create note");
		        	}
		        			            
		            Intent intent2 = new Intent( context, MainActivity.class);
		            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            	startActivity(intent2);
			}
		});
		alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() 
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
		     dialog.cancel();
				
			}
		});
		
		AlertDialog alertD = alertDialogBuilder.create();
		alertD.show();
	}
    /*
    public void deleteNote()
	{
		if(note != null)
		{
			note.close();
			note = null;
		}
		if(mRowId != null)
		{
			mDbHelper.deleteNote(mRowId);
		}
	}*/
    
    /**
     * Calls another activity GET COLOR (Background/Text Color)
     */
    public void showRecentDocuments()
    {
    	Intent intent = new Intent(this, ShowRecentDocuments.class);
    	//startActivity(intent);
    	startActivityForResult(intent, SHOWRECENTDOCUMENTS);
    }
    
    
    /**
     * Calls 2nd activity GET COLOR (Background/Text Color)
     */
    public void getBackgroundColor()
    {
    	Intent intent = new Intent(this, GetColorActivity.class);
    	//startActivity(intent);
    	startActivityForResult(intent, BACKGROUND_COLOR);
    }
    public void getTextColor()
    {
    	Intent intent = new Intent(this, GetColorActivity.class);
    	//startActivity(intent);
    	startActivityForResult(intent, TEXT_COLOR);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // if the results is coming from BACKGROUND COLOR CHANGE.
        if (requestCode == BACKGROUND_COLOR) {

            // check the result code set by the activity
            if (resultCode == RESULT_OK) {

                // get the intent extras and get the value returned
                String value = data.getExtras().getString("value");
        	    
               // String colorValue = token+'.'+value;
                EditText editText = (EditText) findViewById(R.id.message_body);
                
               
                if(value.equals(redColor))
            	{
                	editText.setBackgroundColor(Color.rgb(200, 0, 0));//Color.RED);
                	showToast(redColor);
            	}
                else if(value.equals(greenColor)){
                	editText.setBackgroundColor(Color.GREEN);
                	showToast(greenColor);
                }
                else if(value.equals(blueColor)){
                	editText.setBackgroundColor(Color.BLUE);
                	showToast(blueColor);
                }
                else if(value.equals(blackColor)){
                	editText.setBackgroundColor(Color.BLACK);
                	showToast(blackColor);
                }
                else if(value.equals(cyanColor)){
                	editText.setBackgroundColor(Color.CYAN);
                	showToast(cyanColor);
                }
                else if(value.equals(dkgrayColor)){
                	editText.setBackgroundColor(Color.DKGRAY);
                	showToast(dkgrayColor);
                }
                else if(value.equals(grayColor)){
                	editText.setBackgroundColor(Color.GRAY);
                	showToast(grayColor);
                }
                else if(value.equals(ltgrayColor)){
                	editText.setBackgroundColor(Color.LTGRAY);
                	showToast(ltgrayColor);
                }
                else if(value.equals(magentaColor)){
                	editText.setBackgroundColor(Color.MAGENTA);
                	showToast(magentaColor);
                }
                else if(value.equals(transparentColor)){
                	editText.setBackgroundColor(Color.TRANSPARENT);
                	showToast(transparentColor);
                }
                else if(value.equals(whiteColor)){
                	editText.setBackgroundColor(Color.WHITE);
                	showToast(whiteColor);
                }
                else if(value.equals(yellowColor)){
                	editText.setBackgroundColor(Color.YELLOW);
                	showToast(yellowColor);
                }
                else{
                	editText.setBackgroundColor(Color.WHITE);
                	showToast(whiteColor);
                }
                
            }
        }
        
        // if the results is coming from TEXT COLOR CHANGE.
        if (requestCode == TEXT_COLOR) {

            // check the result code set by the activity
            if (resultCode == RESULT_OK) {

                // get the intent extras and get the value returned
                String value = data.getExtras().getString("value");
        	    
               // String colorValue = token+'.'+value;
                EditText editText = (EditText) findViewById(R.id.message_body);
                
               
                if(value.equals(redColor))
            	{
                	editText.setTextColor(Color.RED);
                	showToastText(redColor);
            	}
                else if(value.equals(greenColor)){
                	editText.setTextColor(Color.GREEN);
                	showToastText(greenColor);
                }
                else if(value.equals(blueColor)){
                	editText.setTextColor(Color.BLUE);
                	showToastText(blueColor);
                }
                else if(value.equals(blackColor)){
                	editText.setTextColor(Color.BLACK);
                	showToastText(blackColor);
                }
                else if(value.equals(cyanColor)){
                	editText.setTextColor(Color.CYAN);
                	showToastText(cyanColor);
                }
                else if(value.equals(dkgrayColor)){
                	editText.setTextColor(Color.DKGRAY);
                	showToastText(dkgrayColor);
                }
                else if(value.equals(grayColor)){
                	editText.setTextColor(Color.GRAY);
                	showToastText(grayColor);
                }
                else if(value.equals(ltgrayColor)){
                	editText.setTextColor(Color.LTGRAY);
                	showToastText(ltgrayColor);
                }
                else if(value.equals(magentaColor)){
                	editText.setTextColor(Color.MAGENTA);
                	showToastText(magentaColor);
                }
                else if(value.equals(transparentColor)){
                	editText.setBackgroundColor(Color.TRANSPARENT);
                	showToastText(transparentColor);
                }
                else if(value.equals(whiteColor)){
                	editText.setTextColor(Color.WHITE);
                	showToastText(whiteColor);
                }
                else if(value.equals(yellowColor)){
                	editText.setTextColor(Color.YELLOW);
                	showToastText(yellowColor);
                }
                else{
                	editText.setTextColor(Color.WHITE);
                	showToastText(whiteColor);
                }
            }
        }
    }
    
    /**
     * Displays Toast with RGB values of given color.
     * 
     * @param color the color
     */
    private void showToast(String str) {
        //String rgbString = "R: " + Color.red(color) + " B: " + Color.blue(color) + " G: " + Color.green(color);
    	String strShow = str.concat(" color selected for Background.");
        Toast.makeText(this, strShow, Toast.LENGTH_SHORT).show();
    }
    
    private void showToastText(String str) {
        //String rgbString = "R: " + Color.red(color) + " B: " + Color.blue(color) + " G: " + Color.green(color);
    	String strShow = str.concat(" color selected for Text.");
        Toast.makeText(this, strShow, Toast.LENGTH_SHORT).show();
    }
    
}

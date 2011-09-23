package org.ale.lectureleaks;

import java.util.ArrayList;
import java.util.List;

import pl.magot.vetch.ancal.AnCal;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainMenuActivity extends ActivityGroup {
    
    GridView gridView;
    private List<MenuCellItem> listItems = new ArrayList<MenuCellItem>(); 
    private SharedPreferences prefs;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //no title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_menu);
        
        gridView = (GridView) findViewById(R.id.gridview);
        
        listItems.add(new MenuCellItem(getString(R.string.menu_cell_1), R.drawable.record, RecorderActivity.class));
        listItems.add(new MenuCellItem(getString(R.string.menu_cell_2), R.drawable.learn, LearnListViewActivity.class));
        listItems.add(new MenuCellItem(getString(R.string.menu_cell_3), R.drawable.schedule, AnCal.class));
        listItems.add(new MenuCellItem(getString(R.string.menu_cell_4), R.drawable.about, AboutActivity.class));
        
        ListItemsAdapter lia = new ListItemsAdapter(listItems);
        gridView.setAdapter(lia);
        
        gridView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Intent i = new Intent(MainMenuActivity.this, listItems.get((arg2)).getLaunchTo());
                startActivity(i);
            }});
        
        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
        final SharedPreferences.Editor editor2;
        String first = prefs.getString("first_time", "feck");
        if(first.contains("feck")){
        	
            editor2 = prefs.edit();
            editor2.putString("first_time", "shitballs");
            editor2.commit();
        	
        	AlertDialog.Builder alert = new AlertDialog.Builder(this);

        	alert.setTitle("Welcome to LectureLeaks!");
        	alert.setMessage("LectureLeaks lets you record, share and listen to your university lectures.\n\nIf you're a student, please enter the school you attend - this helps you categorize your lectures.");

        	// Set an EditText view to get user input 
        	final EditText input = new EditText(this);
        	input.setHint("ex: Boston University");
        	alert.setView(input);

        	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int whichButton) {
        	  String value = input.getText().toString();
        	  if(value.equals("")){
        		  return;
        	  }
        	  final StringBuilder result = new StringBuilder(value.length());
        	  String[] words = value.split("\\s");
        	  for(int i=0,l=words.length;i<l;++i) {
        	    if(i>0) result.append(" ");      
        	    result.append(Character.toUpperCase(words[i].charAt(0)))
        	          .append(words[i].substring(1));

        	  }

        	  editor2.putString("da_school", new String(result));
        	  editor2.commit();
        	  }
        	});

        	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        	  public void onClick(DialogInterface dialog, int whichButton) {
        	    // Canceled.
        	  }
        	});

        	alert.show();
            
           
        }
        
    }
    
    class ListItemsAdapter extends ArrayAdapter<MenuCellItem> {

        public ListItemsAdapter(List<MenuCellItem> items) {
            super(MainMenuActivity.this, android.R.layout.simple_list_item_1, items);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            LayoutInflater inflater = getLayoutInflater();
            convertView = inflater.inflate(R.layout.menu_cell, null);
            
            holder = new ViewHolder();
            holder.text = (TextView)convertView.findViewById(R.id.icon_text);
            ImageView iv = (ImageView)convertView.findViewById(R.id.icon_image);
            TextView tv = (TextView)convertView.findViewById(R.id.icon_text);

            convertView.setTag(holder);

            // Bind the data efficiently with the holder.
            holder.text.setText( listItems.get(position).name );
            
            tv.setText(listItems.get(position).getName());
            iv.setImageResource(listItems.get(position).getImageId());
            
            return convertView;
        }

        private class ViewHolder {
            TextView text;
        }
        
        
    }
    
}
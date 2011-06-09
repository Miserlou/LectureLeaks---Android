package org.ale.lectureleaks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LearnArrayListAdapter extends ArrayAdapter<ArrayList<ArrayList<HashMap<String, String>>>> {

    public LearnArrayListAdapter(Context c, int i) {
        super(c, i);
        r = c.getResources();
        rightArrow = r.getDrawable(R.drawable.rightarrow);
        smRightArrow = r.getDrawable(R.drawable.sm_rightarrow);
    }

    ArrayList<ArrayList<HashMap<String, String>>> items;
    Context c;
    Activity parentActivity;
    Resources r;
    Drawable rightArrow;
    Drawable smRightArrow;
    

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        
        if(convertView == null) {

            LayoutInflater inflater = parentActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_view_row, null);
            
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.toptext);
            holder.desc = (TextView) convertView.findViewById(R.id.bottomtext);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        if(items.get(position).size() <= 0) {
            convertView.setVisibility(View.GONE);
            LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.rsmr_ll);
            ll.setVisibility(View.GONE);
            return convertView;
        }
        HashMap<String, String> r = items.get(position).get(0);
        holder.text.setText(r.get("title"));
        String d = r.get("description");
        if(d != null && d != "null") {
            holder.desc.setText(d);
        }
        else {
            holder.desc.setText("No description available");
        }
        
        // This is a category header
        if(r.containsKey("head") && r.get("head").equals("true")) {
            LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.rsmr_ll);
            holder.text.setTextColor(Color.WHITE);
            holder.desc.setVisibility(View.GONE);
            ll.setBackgroundResource(R.layout.header_bar_gradient);
        }
        else {
            LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.rsmr_ll);
            holder.text.setTextColor(Color.parseColor("#5a96d1"));
            holder.desc.setVisibility(View.VISIBLE);
            ll.setBackgroundColor(Color.WHITE);
            holder.text.setCompoundDrawablesWithIntrinsicBounds(null, null, smRightArrow, null);
        }
        
        return convertView;
    }
    
      public void setContext(Context co) {
      c = co;
    }
      
   public void setParent(Activity a) {
       parentActivity = a;
   }
   
   public void setItems(ArrayList<ArrayList<HashMap<String, String>>> al) {
       if(items == null) {
           items = al;
       }
       else {
           items.clear();
           Iterator<ArrayList<HashMap<String, String>>> i = al.iterator();
           while(i.hasNext()) {
               items.add(i.next());
           }
       }
   }
   
   private class ViewHolder {
       TextView text;
       TextView desc;
   }

public int getCount() {
    return items.size();
}

public long getItemId(int position) {
    return 0;
}

}

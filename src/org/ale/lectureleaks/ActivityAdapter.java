package org.ale.lectureleaks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityAdapter extends BaseAdapter{

    ArrayList<HashMap<String, String>> listItems;
    Context c;
    Activity a;
    
    public void setContext(Context co) {
        c = co;
    }
    
    public void setList(ArrayList<HashMap<String, String>> li) {
        if(listItems == null) {
            listItems = li;
        }
        else if(li != null) {
            listItems.clear();
            Iterator<HashMap<String, String>> i = li.iterator();
            while(i.hasNext()) {
                listItems.add(i.next());
            }
        }
        
    }
    
    public void setParent(Activity ac) {
        a = ac;
    }

    public int getCount() {
        return listItems.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        LayoutInflater inflater = a.getLayoutInflater();

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.activity_item, null);
            
            holder = new ViewHolder();
            holder.text = (TextView)convertView.findViewById(R.id.text);
            ImageView iv = (ImageView)convertView.findViewById(R.id.image);
            holder.desc = (TextView)convertView.findViewById(R.id.subtext);
    
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        if(listItems.get(position).size() <= 0) {
            convertView.setVisibility(View.GONE);
            return convertView;
        }
        
        HashMap<String, String> r = listItems.get(position);
        holder.text.setText(r.get("activity"));
        String d = r.get("description");
        holder.desc.setText(d);
        
        return convertView;
    }

    private class ViewHolder {
        TextView text;
        TextView desc;
    }
    
}

package org.ale.lectureleaks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HashMapItemAdapter extends BaseAdapter{

    List<HashMap<String, String>> listItems;
    Context c;
    Activity a;
    
    public void setContext(Context co) {
        c = co;
    }
    
    public void setList(List<HashMap<String, String>> li) {
        listItems = li;
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
        convertView = inflater.inflate(R.layout.menu_cell, null);
        
        holder = new ViewHolder();
        holder.text = (TextView)convertView.findViewById(R.id.icon_text);
        ImageView iv = (ImageView)convertView.findViewById(R.id.icon_image);
        TextView tv = (TextView)convertView.findViewById(R.id.icon_text);

        convertView.setTag(holder);

        // Bind the data efficiently with the holder.
        holder.text.setText( listItems.get(position).toString() );
        tv.setText(listItems.get(position).toString());
        
        return convertView;
    }

    private class ViewHolder {
        TextView text;
    }
    
}

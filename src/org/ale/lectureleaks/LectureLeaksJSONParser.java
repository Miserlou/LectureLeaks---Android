package org.ale.lectureleaks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LectureLeaksJSONParser implements ParserInterface {
    
    
    String type = "";
    public LectureLeaksJSONParser(String typeOf){
        type = typeOf;
    
    }
    
    public static ArrayList<ArrayList<HashMap<String, String>>> getSchoolsList(String input) throws JSONException{
        
        ArrayList<ArrayList<HashMap<String, String>>> schoolsList = new ArrayList<ArrayList<HashMap<String, String>>>();

        ArrayList<HashMap<String,String>> inner = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> hm;
        JSONObject p;
        String s;
        String ss;
        
        System.out.println(input);
        
        JSONArray root = new JSONArray(input);
        JSONObject fields;
        Iterator<String> baseIterator;
        Iterator<String> fieldsIterator;
        int len = root.length();
        
        for(int i=0;i<len;i++) {
            hm = new HashMap<String, String>();
            p = root.getJSONObject(i);
            baseIterator = p.keys();
            while(baseIterator.hasNext()) {
                s = (String) baseIterator.next();
                System.out.println("S IS");
                System.out.println(s);
                if(s.equals("fields")) {
                    fields = p.getJSONObject(s);
                    fieldsIterator = fields.keys();
                    while(fieldsIterator.hasNext()) {
                        ss = fieldsIterator.next();
                        hm.put(ss, fields.getString(ss));
                    }
                } else {
                    hm.put(s, p.getString(s));
                }
            }
            inner = new ArrayList<HashMap<String,String>>();
            inner.add(hm);
            schoolsList.add(inner);
        }
        
        return schoolsList;
    }
    
public static ArrayList<ArrayList<HashMap<String, String>>> getSchoolList(String input) throws JSONException{
        
        ArrayList<ArrayList<HashMap<String, String>>> schoolsList = new ArrayList<ArrayList<HashMap<String, String>>>();

        ArrayList<HashMap<String,String>> inner = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> hm;
        JSONObject p;
        String s;
        
        input = input.replace("'", "\"");
        input = input.replace(": u\"", ": \"");
        
        JSONArray root = new JSONArray(input);
        Iterator<String> baseIterator;
        int len = root.length();
        
        for(int i=0;i<len;i++) {
            hm = new HashMap<String, String>();
            p = root.getJSONObject(i);
            baseIterator = p.keys();
            while(baseIterator.hasNext()) {
                s = (String) baseIterator.next();
                if(s.equals("subject__name")) {
                    hm.put("name", p.getString(s));
                }
                if(s.equals("course__name")) {
                    hm.put("name", p.getString(s));
                }
                
                hm.put(s, p.getString(s));
            }
            
            inner = new ArrayList<HashMap<String,String>>();
            inner.add(hm);
            schoolsList.add(inner);
        }
        
        return schoolsList;
    }
    
    public ArrayList<ArrayList<HashMap<String, String>>> parse(String input) throws JSONException {
        switch(ParseType.toParseType(type)) {
            case SCHOOLS:
                return getSchoolsList(input);
            case IND_SCHOOL:
                return getSchoolList(input);
        }
        return null;
    }
        

public enum ParseType
{
    SCHOOLS, IND_SCHOOL, NOVALUE;

    public static ParseType toParseType(String str)
    {
        try {
            return valueOf(str);
        } 
        catch (Exception ex) {
            return NOVALUE;
        }
    }   
}

    
}

package org.ale.lectureleaks;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import android.widget.Adapter;

public interface ParserInterface {
    // This is just a regular method so it can return something or
    // take arguments if you like.
    public ArrayList<ArrayList<HashMap<String, String>>> parse(String input) throws JSONException;
}
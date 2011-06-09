package org.ale.lectureleaks;

import android.app.Activity;

public class MenuCellItem {
    public String name;
    public int imageId;
    public Class launchTo;
    
    public MenuCellItem(final String n, final int i, final Class l) {
        name = n;
        imageId = i;
        launchTo = l;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public Class getLaunchTo() {
        return launchTo;
    }
    public void setLaunchTo(Class launchTo) {
        this.launchTo = launchTo;
    }
    
   

}

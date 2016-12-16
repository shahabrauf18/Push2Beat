package com.attribes.push2beat.Utils;

import android.location.Location;

/**
 * Created by android on 12/9/16.
 */

public class Common {
    private static Common Instance = null;
    private Location location;


    private Common() {
    }

    public static Common getInstance()
    {
        if(Instance == null)
        {
            Instance = new Common();
        }
        return Instance;
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public String calulateDistance(String lat, String lng) {
        Location userLocation = new Location("opponentLocation");
        userLocation.setLatitude(Double.parseDouble(lat));
        userLocation.setLongitude(Double.parseDouble(lng));
        int distance = (int) userLocation.distanceTo(Common.getInstance().getLocation());
        return String.valueOf(distance)+"m";
    }

}
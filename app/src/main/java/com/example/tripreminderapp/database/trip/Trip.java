package com.example.tripreminderapp.database.trip;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Trip implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String startPoint;
    private String endPoint;
    private String date;
    private String time;
    private String date_time;
    private String status;
   // private String type;
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private boolean isOK;
    private boolean isAlarmPrepared;

    public Trip(String name, String startPoint, String endPoint, String date, String time, String date_time, String type) {
        this.id = id;
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.time = time;
        this.date_time = date_time;
        this.isAlarmPrepared = false;
        isOK=false;
        //this.type = type;

    }

    public Trip(int id, String name, String startPoint, String endPoint, String date, String time, String date_time, String status, String type, double startLatitude, double startLongitude, double endLatitude, double endLongitude, boolean isOK, boolean isAlarmPrepared) {
        this.id = id;
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.time = time;
        this.date_time = date_time;
        this.status = status;
        //this.type = type;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.isOK = isOK;
        this.isAlarmPrepared = isAlarmPrepared;
    }

    public Trip() {
        this.isAlarmPrepared = false;
        this.isOK = false;
    }

    public Trip(String name, String startPoint, String endPoint, String date, String time) {
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.time = time;
        this.isAlarmPrepared = false;
        isOK=false;
    }

    public Trip(int id, String name, String startPoint, String endPoint, String date, String time) {
        this.id = id;
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;
        this.time = time;
        this.isAlarmPrepared = false;
        isOK=false;
    }


    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public boolean isAlarmPrepared() {
        return isAlarmPrepared;
    }

    public void setAlarmPrepared(boolean alarmPrepared) {
        isAlarmPrepared = alarmPrepared;
    }

//    public String getType() {
//        return type;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getDate_time() {
        return date_time;
    }


    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isOK() { return isOK; }

    public void setOK(boolean OK) {
        this.isOK = OK;
        if(OK == true){
            setStatus("done");
        }else {
            setStatus("Canceled");
        }
    }

//    public void setType(String type) {
//        this.type = type;
//    }


}


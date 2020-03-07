package com.example.weatherapp.Models;

public class weather {
    /*need to add four 0s to the time
    * to get the current time since EPOCH
    * hmmm interesting ex. 1583910000 + 0000 = 15839100000000 (March 11 2020 8pm)
     */
    private String name;
    private int latitude;
    private int longitude;
    private String summary;
    private int temperature;

    public int getTemperatureHigh() {
        return temperatureHigh;
    }

    public void setTemperatureHigh(int temperatureHigh) {
        this.temperatureHigh = temperatureHigh;
    }

    public int getTemperatureLow() {
        return temperatureLow;
    }

    public void setTemperatureLow(int temperatureLow) {
        this.temperatureLow = temperatureLow;
    }

    private int temperatureHigh;
    private int temperatureLow;
    private String icon;
    private double moonPhase;
    private int visibility;
    private String timezone;
    private int nearestStormDistance;

    public weather(String name, int latitude, int longitude, String summary, int temperature, String icon, double moonPhase, int visibility, String timezone, int nearestStormDistance) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.summary = summary;
        this.temperature = temperature;
        this.icon = icon;
        this.moonPhase = moonPhase;
        this.visibility = visibility;
        this.timezone = timezone;
        this.nearestStormDistance = nearestStormDistance;
    }

    public weather () {    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "weather{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", summary='" + summary + '\'' +
                ", temperature=" + temperature +
                ", icon='" + icon + '\'' +
                ", moonPhase=" + moonPhase +
                ", visibility=" + visibility +
                ", timezone='" + timezone + '\'' +
                ", nearestStormDistance=" + nearestStormDistance +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getMoonPhase() {
        return moonPhase;
    }

    public void setMoonPhase(double moonPhase) {
        this.moonPhase = moonPhase;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getNearestStormDistance() {
        return nearestStormDistance;
    }

    public void setNearestStormDistance(int nearestStormDistance) {
        this.nearestStormDistance = nearestStormDistance;
    }
}

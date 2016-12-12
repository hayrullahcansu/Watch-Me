package com.watchme.android.hbclustering.others;

/**
 * Created by hayru on 11/3/2016.
 */
public class HBTime {
  private int hour;
  private int minutes;

  /*
  *  Constructor
  */
  public HBTime(int hour, int minutes) {
	this.hour = hour;
	this.minutes = minutes;
  }

  public HBTime(int pureTime) {
	this.hour = Math.round(pureTime / 60);
	this.minutes = Math.round(pureTime % 60);
  }


  public int getHour() {
	return hour;
  }

  public int getMinutes() {
	return minutes;
  }

  public void setHour(int hour) {
	this.hour = hour;
  }

  public void setMinutes(int minutes) {
	this.minutes = minutes;
  }

  public int getPureTime() {
	return this.hour * 60 + this.minutes;
  }

  public void setPureTime(int pureTime) {
	this.hour = Math.round(pureTime / 60);
	this.minutes = Math.round(pureTime % 60);
  }

  public String getTimeText() {
	return String.format("%2d:%2d", this.hour, this.minutes);
  }
}

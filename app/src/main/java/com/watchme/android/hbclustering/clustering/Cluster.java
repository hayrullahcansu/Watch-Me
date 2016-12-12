package com.watchme.android.hbclustering.clustering;

import com.watchme.android.hbclustering.others.HBTime;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


/**
 * Created by hayru on 11/3/2016.
 */
public class Cluster implements Calculation {

  //region PRIVATE VARIABLES


  private String clusterName;
  private List<Item> items;
  private List<JuniorCluster> juniorClusters;
  private int avgItemsSpaceTime;
  private float calculationTolerantAvgItemsSpaceTime = 0.7777f;
  private int calculationMinumumClusterItemsNumber = 3;
  private int lastCalculatedItemsSize = 0;
  private boolean autoCalculation = false;
  //endregion

  //region GETTER SETTER
  public List<JuniorCluster> getJuniorClusters() {
	return juniorClusters;
  }

  public void setAvgItemsSpaceTime(int avgItemsSpaceTime) {

	this.avgItemsSpaceTime = avgItemsSpaceTime;
  }

  public float getCalculationTolerantAvgItemsSpaceTime() {
	return calculationTolerantAvgItemsSpaceTime;
  }

  public void setCalculationTolerantAvgItemsSpaceTime(float calculationTolerantAvgItemsSpaceTime) {
	this.calculationTolerantAvgItemsSpaceTime = calculationTolerantAvgItemsSpaceTime;
  }

  public int getCalculationMinumumClusterItemsNumber() {
	return calculationMinumumClusterItemsNumber;
  }

  public void setCalculationMinumumClusterItemsNumber(int calculationMinumumClusterItemsNumber) {
	this.calculationMinumumClusterItemsNumber = calculationMinumumClusterItemsNumber;
  }

  public int getLastCalculatedItemsSize() {
	return lastCalculatedItemsSize;
  }

  public void setLastCalculatedItemsSize(int lastCalculatedItemsSize) {
	this.lastCalculatedItemsSize = lastCalculatedItemsSize;
  }
  //endregion

  //region PUBLIC METHODS
  public String getClusterName() {
	return clusterName;
  }

  public void setClusterName(String clusterName) {
	this.clusterName = clusterName;
  }

  @Override
  public void calculateCluster() {
	calculateAvgItemsSpaceTime ();
	if (autoCalculation)
	  calculateMinumumClusterItemsNumber ();
	parseCluster ();
  }

  public int getAvgItemsSpaceTime() {
	return avgItemsSpaceTime;
  }

  public void addNewItem(Item i) {
	if (items == null) createNewItemList ();
	items.add ( i );
  }

  //endregion

  //region PRIVATE METHODS
  private void createNewItemList() {
	items = new LinkedList<> ();
  }

  private void calculateAvgItemsSpaceTime() {
	ListIterator<Item> list = items.listIterator ();
	int avgTemp = 0;
	int cur = 0, prev = 0;
	prev = list.next ().pureTime;
	while (list.hasNext ()) {
	  cur = list.next ().pureTime;
	  avgTemp += (cur - prev);
	  prev = cur;
	}
	try {
	  avgTemp = Math.round ( (avgTemp / (items.size () - 1)) * calculationTolerantAvgItemsSpaceTime );
	  avgItemsSpaceTime = avgTemp;
	} catch (Exception e) {
	  e.printStackTrace ();
	}
  }

  private void calculateMinumumClusterItemsNumber() {
	//
	setCalculationMinumumClusterItemsNumber ( Math.round ( items.size () * 0.33f ) );
  }

  private void calculateTolerantAvgItemsSpaceTime() {
	//setCalculationTolerantAvgItemsSpaceTime((float)items.size());
  }

  private void parseCluster() {
	juniorClusters = new LinkedList<> ();
	Item[] list = items.toArray ( new Item[items.size ()] );
	Item prev = list[0];
	JuniorCluster junior = new JuniorCluster ( this );
	junior.setFirstItem ( prev );
	Item current;
	int totalTime = prev.pureTime;
	int counter = 1;
	int buffer = 0;
	boolean isComplex = false;
	boolean isComplated = false;
	while (!isComplated) {
	  if (buffer == list.length - 1) {
		if (((1439 - list[buffer].pureTime) + list[0].pureTime) <= avgItemsSpaceTime) {
		  isComplex = true;
		  buffer = 0;
		} else {
		  isComplated = true;
		}
	  }
	  current = list[buffer];
	  int diff = current.pureTime - prev.pureTime;
	  if (isComplex) {
		if (((diff + 1439) > avgItemsSpaceTime && buffer == list.length - 1) || diff > avgItemsSpaceTime) {
		  if (counter >= calculationMinumumClusterItemsNumber) {
			junior.setLastItem ( prev );
			HBTime tempTime = new HBTime ( Math.round ( totalTime / counter ) );
			if (tempTime.getPureTime () > 1439)
			  tempTime.setPureTime ( tempTime.getPureTime () - 1439 );
			junior.setAverageOfTime ( tempTime );
			juniorClusters.add ( junior );
			junior.setSize ( counter );
			junior.setRatioAccumulation ( (float) counter / items.size () );
			juniorClusters.remove ( 0 );     //ilk grup siliniyor. zaten eklendiği için !
			break;
		  }
		} else {
		  totalTime += current.pureTime + 1439;
		  counter++;
		}
	  } else {
		if (diff > avgItemsSpaceTime) {
		  if (counter >= calculationMinumumClusterItemsNumber) {
			junior.setLastItem ( prev );
			junior.setAverageOfTime ( new HBTime ( Math.round ( totalTime / counter ) ) );
			juniorClusters.add ( junior );
			junior.setSize ( counter );
			junior.setRatioAccumulation ( (float) counter / items.size () );
		  }
		  junior = new JuniorCluster ( this );
		  junior.setFirstItem ( current );
		  totalTime = current.pureTime;
		  counter = 1;
		} else {
		  totalTime += current.pureTime;
		  counter++;
		}
	  }
	  prev = current;
	  buffer++;
	}

	if (junior.getLastItem () == null && counter >= calculationMinumumClusterItemsNumber) {
	  junior.setLastItem ( prev );
	  junior.setAverageOfTime ( new HBTime ( Math.round ( totalTime / counter ) ) );
	  juniorClusters.add ( junior );
	  junior.setSize ( counter );
	  junior.setRatioAccumulation ( (float) counter / items.size () );
	}
	System.out.println ( "\n\n\nProgress succesfully and found " + juniorClusters.size () + " juniorClusters\n" +
		  "cluster name: " + getClusterName () + "\n" +
		  "Average Items Time Spaces: " + getAvgItemsSpaceTime () );

	StringBuilder sb = new StringBuilder ( "\n\n" );
	for (int i = 0; i < juniorClusters.size (); i++) {
	  JuniorCluster jc = juniorClusters.get ( i );
	  sb.append (
			String.format (
				  "juniorCluster         ---->%5d \n" +
						"firstTime is               %5d\n" +
						"avg is                     %5d\n" +
						"lastTime is                %5d\n",
				  (i + 1), jc.getFirstItem ().pureTime, jc.getAverageOfTime ().getPureTime (), jc.getLastItem ().pureTime ) );
	  sb.append ( String.format ( "size of juniorCluster :    %5d\n" +
			"ratio accumulation :       %5.1f\n", jc.getSize (), jc.getRatioAccumulation () ) );
	  sb.append ( String.format ( "average of time is         %2d:%2d\n\n", jc.getAverageOfTime ().getHour (), jc.getAverageOfTime ().getMinutes () ) );
	}
	System.out.println ( sb.toString () );
	this.setLastCalculatedItemsSize ( items.size () );
	items.clear ();
  }

  //endregion
}

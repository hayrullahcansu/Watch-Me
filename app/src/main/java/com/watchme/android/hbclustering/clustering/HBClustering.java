package com.watchme.android.hbclustering.clustering;


import com.watchme.android.hbclustering.hbexception.NoSuggestionAClusterError;
import com.watchme.android.hbclustering.others.HBTime;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by hayru on 11/3/2016.
 */
public class HBClustering {


  private ArrayList<Cluster> clusterList;
  private IResult iResult;

  public HBClustering() {
	createList();
  }

  public HBClustering(IResult _iResult) {
	this.iResult = _iResult;
	createList();
  }

  public void generateRandomItems() {
	Cluster cluster = new Cluster();
	Random random = new Random();

	//region Facebook Items Generating
	cluster.setClusterName("com.mark.facebook");
	Item item = new Item(10);
	cluster.addNewItem(item);
	item = new Item(11);
	cluster.addNewItem(item);
	item = new Item(50);
	cluster.addNewItem(item);
	item = new Item(55);
	cluster.addNewItem(item);
	item = new Item(56);
	cluster.addNewItem(item);
	item = new Item(100);
	cluster.addNewItem(item);
	item = new Item(110);
	cluster.addNewItem(item);
	item = new Item(300);
	cluster.addNewItem(item);
	item = new Item(330);
	cluster.addNewItem(item);
	item = new Item(1080);
	cluster.addNewItem(item);
	item = new Item(1090);
	cluster.addNewItem(item);
	item = new Item(1110);
	cluster.addNewItem(item);
	item = new Item(1130);
	cluster.addNewItem(item);
	item = new Item(1131);
	cluster.addNewItem(item);
	item = new Item(1500);
	cluster.addNewItem(item);
	item = new Item(1550);
	cluster.addNewItem(item);
	item = new Item(1800);
	cluster.addNewItem(item);
	item = new Item(1900);
	cluster.addNewItem(item);
	item = new Item(2000);
	cluster.addNewItem(item);
	item = new Item(2500);
	cluster.addNewItem(item);
	item = new Item(2550);
	cluster.addNewItem(item);
	item = new Item(2570);
	cluster.addNewItem(item);
	addCluster(cluster);
	//endregion

	//region Twitter Items Generating
	cluster = new Cluster();
	cluster.setClusterName("com.twi.twitter");
	item = new Item(10);
	cluster.addNewItem(item);
	item = new Item(360);
	cluster.addNewItem(item);
	item = new Item(365);
	cluster.addNewItem(item);
	item = new Item(1050);
	cluster.addNewItem(item);
	item = new Item(1060);
	cluster.addNewItem(item);
	item = new Item(1061);
	cluster.addNewItem(item);
	item = new Item(1062);
	cluster.addNewItem(item);
	item = new Item(1070);
	cluster.addNewItem(item);

	item = new Item(1400);
	cluster.addNewItem(item);
	item = new Item(1410);
	cluster.addNewItem(item);
	//endregion
	addCluster(cluster);
  }

  public JuniorCluster getSuggestion(HBTime time) throws NoSuggestionAClusterError {
	ListIterator<Cluster> list = clusterList.listIterator();
	JuniorCluster resultJunior = null;
	int diffToJuniorAvgTime = 100000;
	if (list.hasNext()) {
	  while (list.hasNext()) {
		ListIterator<JuniorCluster> juniors = list.next().getJuniorClusters().listIterator();
		while (juniors.hasNext()) {
		  JuniorCluster junior = juniors.next();
		  if (time.getPureTime() >= junior.getFirstItem().pureTime && time.getPureTime() <= junior.getLastItem().pureTime) {
			//this junior's time concur with parameter's time
			int tempAvg = Math.abs(time.getPureTime() - junior.getAverageOfTime().getPureTime());
			if (tempAvg < diffToJuniorAvgTime)   //this junior's time is closer than previous junior;
			{
			  diffToJuniorAvgTime = tempAvg;
			  resultJunior = junior;
			}
		  }
		}
	  }
	} else           //there is not a cluster you cant get a suggestion
	{
	  throw new NoSuggestionAClusterError("ERRKOD:1 Öneri yapılamadı, Liste boş");
	}
	if (resultJunior == null) {
	  throw new NoSuggestionAClusterError("ERRKOD:2 Öneri yapılamadı.");
	} else return resultJunior;
  }

  public void  calculateAllClusters() {
	for (int i = 0; i < clusterList.size(); i++) {
	  clusterList.get(i).calculateCluster();
	}
  }

  public void addCluster(Cluster cluster) {
	if (clusterList == null) createList();
	if (cluster != null)
	  clusterList.add(cluster);
  }

  private void createList() {
	clusterList = new ArrayList<>();
  }

  public ArrayList<Cluster> getClusterList() {
	return clusterList;
  }

}

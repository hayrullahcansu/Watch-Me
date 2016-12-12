package com.watchme.android.hbclustering.clustering;


import com.watchme.android.hbclustering.others.HBTime;

/**
 * Created by hayru on 11/4/2016.
 */
public class JuniorCluster {

    private Cluster owner;
    private Item firstItem;
    private Item lastItem;
    private HBTime averageOfTime;
    private float ratioAccumulation = 0.0f;
    private int size = 0;

    public JuniorCluster(Cluster _cluster) {
        this.owner = _cluster;
    }

    public JuniorCluster(Cluster _cluster,Item _firstItem, Item _lastItem, int _averageOfTime) {
        this.owner = _cluster;
        this.firstItem = _firstItem;
        this.lastItem = _lastItem;
        this.averageOfTime = new HBTime(_averageOfTime);
    }

    //region GETTER SETTER
    public Item getFirstItem() {
        return firstItem;
    }

    public void setFirstItem(Item firstItem) {
        this.firstItem = firstItem;
    }

    public Item getLastItem() {
        return lastItem;
    }

    public void setLastItem(Item lastItem) {
        this.lastItem = lastItem;
    }

    public HBTime getAverageOfTime() {
        return averageOfTime;
    }

    public void setAverageOfTime(HBTime averageOfTime) {
        this.averageOfTime = averageOfTime;
    }

    public float getRatioAccumulation() {
        return ratioAccumulation;
    }

    public int getSize() {
        return size;
    }

    public void setRatioAccumulation(float ratioAccumulation) {
        this.ratioAccumulation = ratioAccumulation;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Cluster getOwner() {
        return owner;
    }
    //endregion


}

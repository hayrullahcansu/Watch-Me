package com.watchme.android.hbclustering.clustering;

import com.watchme.android.hbclustering.others.HBTime;

/**
 * Created by hayru on 11/3/2016.
 */
public class Item {
  /**
   * pureTime = (hours * 60) + minutes
   */
  public int pureTime;

  public Item(int pureTime) {
	this.pureTime = pureTime;
  }

  public String getTextTime() {
	return new HBTime(pureTime).getTimeText();
  }
}

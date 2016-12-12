package com.watchme.android.hbclustering.clustering;

import java.util.ArrayList;

/**
 * Created by hayru on 11/6/2016.
 */
public interface IResult {
  public void onResult(boolean isSuccess, ArrayList<JuniorCluster> juniorClusters);
}


package com.watchme.android.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.watchme.android.R;
import com.watchme.android.adapters.HBDatabaseAdapter;
import com.watchme.android.hbclustering.clustering.Cluster;
import com.watchme.android.hbclustering.clustering.HBClustering;
import com.watchme.android.hbclustering.clustering.IResultOfOrganizingItems;
import com.watchme.android.hbclustering.clustering.JuniorCluster;
import com.watchme.android.utils.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hayru on 11/11/2016.
 */
public class ActivityStatics extends Activity implements IResultOfOrganizingItems {
  TextView textView;
  LinearLayout ll;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_statics);
	initialize();
	getStatics();
  }

  private void getStatics() {
	HBClustering hb = new HBClustering();
	HBDatabaseAdapter.getInstance().setContextForDB(this);
	HBDatabaseAdapter.getInstance().OrganizeItems(hb, this);
  }


  private void initialize() {
	ll = (LinearLayout) findViewById(R.id.ll_statics_list_cluster);
  }

  private void addCluster(Cluster c) {
	StringBuilder sb = new StringBuilder();
	final ClusterTextView tv = new ClusterTextView(this, c);
	sb.append(String.format(
			"Küme Adı     :%17s\n" +
					"Giriş Sayisi :   %17d\n" +
					"Küçük Küme   :%17d",
			c.getClusterName(),
			c.getLastCalculatedItemsSize(),
			c.getJuniorClusters().size()));
	tv.setText(sb.toString());
	tv.setOnClickListener(new View.OnClickListener() {
	  @Override
	  public void onClick(View view) {
		//TODO: Küçültülebilir
		String temp = "";
		Cluster cluster = tv.getCluster();
		temp += String.format("Ana Küme Adı :%20s\n\n", cluster.getClusterName());
		List<JuniorCluster> list = cluster.getJuniorClusters();
		for (int i = 0; i < list.size(); i++) {
		  JuniorCluster jc = list.get(i);
		  temp += String.format(
				  "Küçük küme indis :%5d\n" +
						  "Başlangıç        :%5s\n" +
						  "Ortalama         :%5s\n" +
						  "Son              :%5s\n" +
						  "Veri sayısı      :%5d\n" +
						  "Yığılma Oranı    :%3.2f\n\n\n",
				  (i + 1),
				  jc.getFirstItem().getTextTime(),
				  jc.getAverageOfTime().getTimeText(),
				  jc.getLastItem().getTextTime(),
				  jc.getSize(),
				  jc.getRatioAccumulation()
		  );
		}
		final Dialog dialog = new Dialog(ActivityStatics.this);
		dialog.setContentView(R.layout.dialog_cluster_details);
		final TextView textView = (TextView) dialog.findViewById(R.id.tv_dialog_cluster_details);
		textView.setText(temp);
		final Button button = (Button) dialog.findViewById(R.id.b_close);
		button.setOnClickListener(new View.OnClickListener() {
		  @Override
		  public void onClick(View view) {
			dialog.dismiss();
		  }
		});
		dialog.show();
	  }
	});
	LinearLayout.LayoutParams lytParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	lytParams.gravity = Gravity.CENTER_HORIZONTAL;
	tv.setLayoutParams(lytParams);
	ll.addView(tv);
  }

  @Override
  public void onResultOfOrganizingItems(boolean isSuccess, String resultMessage, HBClustering hbClustering) {
	if (isSuccess) {
	  ArrayList<Cluster> clusters = hbClustering.getClusterList();
	  for (int i = 0; i < clusters.size(); i++) {
		Cluster clus = clusters.get(i);
		addCluster(clus);
	  }
	} else {
	  Message.message(this, resultMessage);
	}


  }


  public class ClusterTextView extends TextView {
	Cluster cluster;

	public ClusterTextView(Context context, Cluster _cluster) {
	  super(context);
	  this.cluster = _cluster;
	}

	public Cluster getCluster() {
	  return this.cluster;
	}
  }
}

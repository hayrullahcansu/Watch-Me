/*
package com.watchme.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.watchme.android.R;

import java.util.HashMap;
import java.util.List;

*/
/**
 * Created by hayru on 11/11/2016.
 *//*

public class ExpandableListAdapter extends BaseExpandableListAdapter {
  private Context _context;
     
  private List<String> _listDataHeader; // header titles
      // child data in format of header title, child title
  private HashMap<String, List<String>> _listDataChild;

  public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
	this._context = context;
	this._listDataHeader = listDataHeader;
	this._listDataChild = listChildData;
  }

  @Override
  public int getGroupCount() {
	return 0;
  }

  @Override
  public int getChildrenCount(int i) {
	return 0;
  }

  @Override
  public Object getGroup(int i) {
	return null;
  }

  @Override
  public Object getChild(int i, int i1) {
	return this._listDataChild.get(this._listDataHeader.get(i)).get(i1);
  }

  @Override
  public long getChildId(int i, int i1) {
	return i1;
  }

  @Override
  public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
	return null;
  }

  @Override
  public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
	final String childText = (String)getChild(i,i1);
	if(view == null)
	{
	  LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  view = infalInflater.inflate(R.layout.cluster_info,null);
	}
	TextView txtListChild = (TextView)view.findViewById(R.id.tv_cluster_name);
	return
  }

  @Override
  public boolean isChildSelectable(int i, int i1) {
	return false;
  }
}
*/

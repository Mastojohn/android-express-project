package com.app.express.activity;

import java.util.List;

import com.app.express.dummy.DeliveryContent;
import com.app.express.dummy.DeliveryContent.DeliveryItem;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class DeliveryListArrayAdapter extends ArrayAdapter<DeliveryContent.DeliveryItem> {

	public DeliveryListArrayAdapter(Context context, int resource, int textViewResourceId, List<DeliveryItem> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return super.getView(position, convertView, parent);
	}

	

}

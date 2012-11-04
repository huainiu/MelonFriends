package com.melonsail.app.melonfriends.controller;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melonsail.app.melonfriends.R;
import com.melonsail.app.melonfriends.activities.MelonFriendsServiceConnection;
import com.melonsail.app.melonfriends.sns.SnsOrg;
import com.melonsail.app.melonfriends.sns.SnsUtil;
import com.melonsail.app.melonfriends.utils.Const;
import com.melonsail.app.melonfriends.utils.FlurryUtil;
import com.melonsail.app.melonfriends.utils.Pref;
import com.melonsail.app.melonfriends.views.listener.TitleBarListener;

public class LeftPanelController implements Controller {
	
	private static final String TAG = "LeftPanelView";

	private Activity zActivity;
	
	private ListView acntLstV;
	private ListView settingLstV;
	private ListView feedBksLstV;
	
	private LstAdapter snsAccntLstAdapter;
	private LstAdapter settingLstAdapter;
	private LstAdapter feedBksLstAdapter;
	
	MelonFriendsServiceConnection mServiceConn;
	SnsOrg mSnsOrg;
	
	public LeftPanelController() {
		
	}
	
	public void setServiceConnection(MelonFriendsServiceConnection mServiceConn) {
		this.mServiceConn = mServiceConn;
	};
	
	public void setSNSOrg(SnsOrg mSnsOrg) {
		this.mSnsOrg = mSnsOrg;
	}

	public void RefreshView()
	{
		if(this.snsAccntLstAdapter != null)
		{
			this.snsAccntLstAdapter.notifyDataSetChanged();
		}
	}

	public void InitContent(Activity activity) {
		this.zActivity = activity;
		
		this.snsAccntLstAdapter = new LstAdapter(activity,mSnsOrg.fGetSnsList(), Const.SETTING_ACNT);
		this.settingLstAdapter = new LstAdapter(activity,Const.SETTING_BASIC_GROUPS, Const.SETTING_BASIC);
		this.feedBksLstAdapter = new LstAdapter(activity,Const.SETTING_FEEDBACKS_GROUPS, Const.SETTING_FEEDBACKS);
		
		int index = Pref.getMyIntPref(zActivity.getApplicationContext(), Const.SETTING_BASIC+"_UPT_FREQ");
		if(index <0) index = 1;
		if(index >= 0 && index < Const.SETTING_UPT_FREQ_BTN_TEXT.length)
		{
			this.settingLstAdapter.SetDtlText(new String[]{Const.SETTING_UPT_FREQ_BTN_TEXT[index]});
		}
		
		this.acntLstV = (ListView) activity.findViewById(R.id.signOnLst);
		this.acntLstV.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mSnsOrg.fToggleSnsSelection(Const.SNSGROUPS[position]);
			}

		});
		
		this.settingLstV = (ListView) activity.findViewById(R.id.settingLst);
		this.settingLstV.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent,
					android.view.View view, int position, long id) {
				
				LaunchDialog(Const.SETTING_BASIC,Const.SETTING_BASIC_GROUPS[position]);
				
			}});
		
		this.feedBksLstV = (ListView) activity.findViewById(R.id.feedBksLst);
		this.feedBksLstV.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent,
					android.view.View view, int position, long id) {
				LaunchDialog(Const.SETTING_FEEDBACKS,Const.SETTING_FEEDBACKS_GROUPS[position]);
			}});
	}


	public void InitTitle(Activity activity, TitleBarListener titleBarListener) {
	}


	public void LoadView(Bundle loadData) {
		//super.LoadView(loadData);
		this.acntLstV.setAdapter(snsAccntLstAdapter);
		this.settingLstV.setAdapter(settingLstAdapter);
		this.feedBksLstV.setAdapter(feedBksLstAdapter);
	}
	
	
	private class LstAdapter extends BaseAdapter
	{
		private LayoutInflater viewInflator;
		private String[] displayArray;
		private ArrayList<SnsUtil> arrayList;
		private String grpName;
		private String[] dtlText;
		
		public LstAdapter(Activity activity, String[] displayArray, String grpName)
		{
			viewInflator = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.displayArray = displayArray;
			this.grpName = grpName;
		}
		
		public LstAdapter(Activity activity, ArrayList<SnsUtil> arrayList, String grpName)
		{
			viewInflator = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.arrayList = arrayList;
			this.grpName = grpName;
		}
		

		@Override
		public int getCount() {
			//return Const.SNSGROUPS.length;
			return (displayArray != null) ? this.displayArray.length : arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			//return Const.SNSGROUPS[position];
			return (displayArray != null) ? displayArray[position] : arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public android.view.View getView(final int position,
				android.view.View convertView, ViewGroup parent) {
			
			
			if(convertView == null)
        	{
        		convertView= viewInflator.inflate(R.layout.fh_login_account_item, null);
        	}
			
			TextView textView = (TextView) convertView.findViewById(R.id.snsLogin);
            //textView.setText(Const.SNSGROUPS[position]);
			if (displayArray!=null) {
				textView.setText(displayArray[position]);
			} else {
				textView.setText(arrayList.get(position).fGetSNSName());
			}
            
            //ListView listView = (ListView) convertView.findViewById(R.id.snsAccountsLst);
			final ImageView imgView = (ImageView) convertView.findViewById(R.id.snsStatusInd);
			final TextView dtlView = (TextView) convertView.findViewById(R.id.txtDetail);
			if(this.grpName.equals(Const.SETTING_ACNT)) {
				setSnsStatus(imgView, mSnsOrg.fGetSnsList().get(position).fIsActive());
			}
			
			final ImageView logView = (ImageView) convertView.findViewById(R.id.snsLogo);
			
			if(this.grpName.equals(Const.SETTING_ACNT)) {
				logView.setImageResource(mSnsOrg.fGetLogoMap().get(arrayList.get(position).fGetSNSName()));
			}
			else if(this.grpName.equals(Const.SETTING_BASIC) && ((String)this.getItem(position)).contains(Const.SETTING_BASIC_GROUPS[0]))
			{
				logView.setImageResource(R.drawable.fh_update_time);
				imgView.setVisibility(android.view.View.GONE);
				dtlView.setVisibility(android.view.View.VISIBLE);
				
				if(this.dtlText != null && dtlText.length > position && dtlText[position].length() > 0)
				dtlView.setText(dtlText[position]);
			}
			else if(this.grpName.equals(Const.SETTING_FEEDBACKS) && ((String)this.getItem(position)).equals(Const.SETTING_FEEDBACKS_GROUPS[1]))
			{
				logView.setImageResource(R.drawable.fh_rate);
			}
			else if(this.grpName.equals(Const.SETTING_FEEDBACKS) && ((String)this.getItem(position)).equals(Const.SETTING_FEEDBACKS_GROUPS[2]))
			{
				logView.setImageResource(R.drawable.fh_help_icon);
			}
			else if(this.grpName.equals(Const.SETTING_FEEDBACKS) && ((String)this.getItem(position)).equals(Const.SETTING_FEEDBACKS_GROUPS[0]))
			{
				logView.setImageResource(R.drawable.fh_feedback_icon);
			}
			
			return convertView;
		}


		private void setSnsStatus(ImageView imgView, boolean selected) {
			
			if(selected)
			{
				imgView.setImageResource(R.drawable.fh_remove);
			}
			else
			{
				imgView.setImageResource(R.drawable.fh_add);
			}
		}
		
		private void LaunchDialog(String grpName, String optionName)
		{
			int displayView = -1;
			int themeId = -1;
			if(grpName.equals(Const.SETTING_BASIC) && optionName.contains(Const.SETTING_BASIC_GROUPS[0]))
			{
				displayView = R.layout.fh_upt_req_layout;
				themeId = android.R.style.Theme_Dialog;
			}
			else if (grpName.equals(Const.SETTING_FEEDBACKS) && optionName.equals(Const.SETTING_FEEDBACKS_GROUPS[1]))
			{
				displayView = R.layout.fh_rate_layout;
				themeId = android.R.style.Theme_Dialog;
			}
			else if(grpName.equals(Const.SETTING_FEEDBACKS) && optionName.equals(Const.SETTING_FEEDBACKS_GROUPS[0]))
			{
				displayView = R.layout.fh_feedback_layout;
			}
			else if (grpName.equals(Const.SETTING_FEEDBACKS) && optionName.equals(Const.SETTING_FEEDBACKS_GROUPS[2]))
			{
				displayView = R.layout.fh_help_layout;
			}
			FlurryUtil.logEvent(TAG + ":LaunchDialog", grpName);
			popUpDialogActivity(displayView,optionName,themeId);
		}


		private void popUpDialogActivity(int displayView, String optionName, int themeId) {
			
			if(displayView > 0)
			{
//				Intent intent = new Intent(zActivity,FHDialogActivity.class);
//				intent.putExtra(Const.DIALOG_VIEW_ID, displayView);
//				intent.putExtra(Const.SETTING_REQ_KEY, optionName);
//				intent.putExtra(Const.DIALOG_THEME_ID, themeId);
//				zActivity.startActivityForResult(intent, Const.CD_REQ_DIALOG);
			}
			else
			{
				Toast.makeText(zActivity, "Invalid View", 1000);
			}
		}
		public void SetDtlText(String[] dtlText)
		{
			this.dtlText = dtlText;
		}
	}


	
	private void LaunchDialog(String grpName, String optionName)
	{
		int displayView = -1;
		int themeId = -1;
		if(grpName.equals(Const.SETTING_BASIC) && optionName.contains(Const.SETTING_BASIC_GROUPS[0]))
		{
			displayView = R.layout.fh_upt_req_layout;
			themeId = android.R.style.Theme_Dialog;
		}
		else if (grpName.equals(Const.SETTING_FEEDBACKS) && optionName.equals(Const.SETTING_FEEDBACKS_GROUPS[1]))
		{
			displayView = R.layout.fh_rate_layout;
			themeId = android.R.style.Theme_Dialog;
		}
		else if(grpName.equals(Const.SETTING_FEEDBACKS) && optionName.equals(Const.SETTING_FEEDBACKS_GROUPS[0]))
		{
			displayView = R.layout.fh_feedback_layout;
		}
		else if (grpName.equals(Const.SETTING_FEEDBACKS) && optionName.equals(Const.SETTING_FEEDBACKS_GROUPS[2]))
		{
			displayView = R.layout.fh_help_layout;
		}
		
		popUpDialogActivity(displayView,optionName,themeId);
	}


	private void popUpDialogActivity(int displayView, String optionName, int themeId) {
		
		if(displayView > 0)
		{
//			Intent intent = new Intent(zActivity,FHDialogActivity.class);
//			intent.putExtra(Const.DIALOG_VIEW_ID, displayView);
//			intent.putExtra(Const.SETTING_REQ_KEY, optionName);
//			intent.putExtra(Const.DIALOG_THEME_ID, themeId);
//			zActivity.startActivityForResult(intent, Const.CD_REQ_DIALOG);
		}
		else
		{
			Toast.makeText(zActivity, "Invalid View", 1000);
		}
	}
	
	
	public void DialogCallBack(Intent data) {
		
		if(data.getIntExtra(Const.DIALOG_VIEW_ID, -1) == R.layout.fh_upt_req_layout)
		{
			int index = data.getIntExtra(Const.SETTING_BASIC_GROUPS[0]+"_SET", -1);
			if(index >=0)
			{
				this.settingLstAdapter.SetDtlText(new String[]{Const.SETTING_UPT_FREQ_BTN_TEXT[index]});
				this.settingLstAdapter.notifyDataSetChanged();
			}
		}
		
	}

	@Override
	public void fRefreshPanelView() {
		snsAccntLstAdapter.notifyDataSetChanged();
	}

	@Override
	public void fRefreshContentView(String snsName) {
		
	}

}

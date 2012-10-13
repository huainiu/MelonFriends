package com.melonsail.app.melonfriends.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;

import com.melonsail.app.melonfriends.R;
import com.melonsail.app.melonfriends.activities.MelonFriendsServiceConnection;
import com.melonsail.app.melonfriends.activities.PublishActivity;
import com.melonsail.app.melonfriends.sns.SnsOrg;
import com.melonsail.app.melonfriends.utils.Const;
import com.melonsail.app.melonfriends.views.PullToRefreshListView;
import com.melonsail.app.melonfriends.views.PullToRefreshListView.OnRefreshListener;
import com.melonsail.app.melonfriends.views.SlidingPanel;
import com.melonsail.app.melonfriends.views.SlidingPanel.PanelSlidingListener;
import com.melonsail.app.melonfriends.views.TabPageIndicator;

public class MainController implements Controller {
	private static final String TAG = "MainController";
	
	private int iContentLayoutId;
	private int iTitleLayoutId;
	
	public Activity mActivity;
	
	private ViewPager mPager;
	private TabPageIndicator mIndicator;
	private FragmentStatePagerAdapter mPagerAdapter;
	private SlidingPanel mSlidingPanel;
	
	private LeftPanelController mleftPanelController;
	private static SnsOrg mSnsOrg;
	
	//private LeftPanelView leftPanelView;
	
	public MainController(Activity activity) {
		this.mActivity = activity;
		this.iContentLayoutId = R.layout.fh_main_ui;
		this.iTitleLayoutId = R.layout.fh_title_bar;
	}

	private static MelonFriendsServiceConnection mServiceConn;
    public void fBindService() {
		if(mServiceConn == null) {
			this.mServiceConn = new MelonFriendsServiceConnection(mActivity);
		}
		this.mServiceConn.fBindToService();
	}
    public void fUnBindService() {
		if(this.mServiceConn != null) {
			this.mServiceConn.fUnBindToService();
		}
	}
	public void fInit() {
		fInitSNSOrg();
		fInitTitle();
		fInitContent();
		
		fInitLeftPanelController();
		fBindService();
	}
	
	public void fInitSNSOrg() {
		mSnsOrg = new SnsOrg(mActivity);
	}
	
	public void fInitTitle() {
		mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	private void fInitSlidingPanel() {
		mSlidingPanel = (SlidingPanel) mActivity.findViewById(R.id.slidepanel);
		mSlidingPanel.SetAlignViewId(R.id.leftPanelLayout);
		mSlidingPanel.setPanelSlidingListener(new PanelSlidingListener(){
			@Override
			public void onSlidingDownEnd() {
			}

			@Override
			public void onSlidingUpEnd() {
			}
		});
    }
	
	private void fInitTitleButtons() {
    	ImageButton btnSetting = (ImageButton) mActivity.findViewById(R.id.leftpanelbtn);
    	btnSetting.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mSlidingPanel.Slide2Left();
			}
		});
    	
    	ImageButton btnPub = (ImageButton) mActivity.findViewById(R.id.writefeedbtn);
    	btnPub.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!mSlidingPanel.isOpen()) {
					Intent intent = new Intent(mActivity, PublishActivity.class);
					mActivity.startActivity(intent);
				} else {
					mSlidingPanel.Slide2Right();
				}
			}
    	});
    	
    	ImageButton btnRefresh = (ImageButton) mActivity.findViewById(R.id.refreshbtn);
    	btnRefresh.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(!mSlidingPanel.isOpen()) {
					int index = mPager.getCurrentItem();
					//SnsFeedListFragment lstFrag = (SnsFeedListFragment) ((FragmentActivity)mActivity).getSupportFragmentManager().findFragmentByTag(((SnsPagerAdapter)mPagerAdapter).getPageTitle(index).toString());
					SnsFeedListFragment lstFrag = (SnsFeedListFragment) mPagerAdapter.getItem(index);
					
					if(lstFrag != null){
						final ListView lv = (ListView) lstFrag.getListView();
						lv.post(new  Runnable(){
							@Override
							public void run() {
								lv.setSelection(0);						
							}
						});
					}	
				}
			}
		});
    		  	
    }
	
	private void fInitPager() {
		if(mActivity instanceof FragmentActivity)  {
			mPagerAdapter = new SnsPagerAdapter(((FragmentActivity)mActivity).getSupportFragmentManager());
		}
		
		mPager = (ViewPager) mActivity.findViewById(R.id.snsPager);
		mPager.setAdapter(mPagerAdapter);
		mIndicator = (TabPageIndicator) mActivity.findViewById(R.id.snsTabIndicator);
		mIndicator.setViewPager(mPager);
	}
	
	public void fInitContent() {
		
		mActivity.setContentView(iContentLayoutId);
		
		fInitPager();

		fInitSlidingPanel();
		fInitTitleButtons();
		//InitGuide(mActivity);
	}
	
	private void fInitLeftPanelController() {
		
		mleftPanelController = new LeftPanelController();
		mleftPanelController.setSNSOrg(mSnsOrg);
		mleftPanelController.InitContent(mActivity);
		mleftPanelController.LoadView(null);
		mleftPanelController.setServiceConnection(mServiceConn);
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mSnsOrg.fGetSnsInstance(Const.SNS_FACEBOOK).onComplete(requestCode, resultCode, data);
	}
	public void fAnaylseIntent(Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (!mSlidingPanel.isActivated()) {
			mActivity.finish();
		}
	}
	
	public class SnsPagerAdapter extends FragmentStatePagerAdapter
	{
		FragmentManager mFragmentManager;
		
		public SnsPagerAdapter(FragmentManager fm) {
			super(fm);
			this.mFragmentManager = fm;
		}
		
		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}

		@Override
		public Fragment getItem(int pos) {
			String sFragmentTag = mSnsOrg.fGetActiveSnsList().get(pos).fGetSNSName();
			SnsFeedListFragment snsFeedListFragment = SnsFeedListFragment.newInstance(pos);
			try {
				mFragmentManager.beginTransaction().add(snsFeedListFragment, sFragmentTag).commit();
			} catch (Exception e) {
				Log.i(TAG, "Error + "+e.toString());
			}
			return snsFeedListFragment;
		}

		@Override
		public int getCount() {
			return mSnsOrg.fGetActiveSnsList().size();
		}
		
		@Override
		public CharSequence getPageTitle(int pos) {
			return mSnsOrg.fGetActiveSnsList().get(pos).fGetSNSName();
		}
		
		public int getTitleCount() {
			return mSnsOrg.fGetActiveSnsList().size();
		}
		
	}
	
	
	public static class SnsFeedListFragment extends ListFragment {
		 //private android.view.View v;
		 private String snsName;
		 private int position;
		 private PullToRefreshListView lstViewFeedPreview;
		 private int iCountScrollEvent;
		 private boolean bScrolling;
		 private int iPrevScrollState;
		 

		static SnsFeedListFragment newInstance(int pos) {
			 SnsFeedListFragment snsFeedListFragment = new SnsFeedListFragment();
			 
			 Bundle data = new Bundle();
			 data.putInt(Const.SNS, pos);
			 snsFeedListFragment.setArguments(data);
			 return snsFeedListFragment;
		 }

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			position = getArguments() != null ? getArguments().getInt(Const.SNS): 0;
			snsName = getTag();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fh_feed_list, container, false);
			return v;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			this.setListAdapter(mSnsOrg.fGetMainViewFeedAdapterList().get(position));
			lstViewFeedPreview = (PullToRefreshListView) this.getListView();
			lstViewFeedPreview.setOnRefreshListener(new OnRefreshListener() {
				@Override
				public void onRefresh() {
					Log.i(TAG, "Request service to retrieve new feed");
					mServiceConn.fSendMessage(Const.MSG_SERVICE_GET_NEWFEED, position, 0);
				}
			});
		}
		 
	}


	@Override
	public void fRefreshView() {
		//refresh toolbars
		mleftPanelController.fRefreshView();
		mPagerAdapter.notifyDataSetChanged();
		mIndicator.notifyDataSetChanged();
		
		//refresh the content
		//TODO:how to
	}

}

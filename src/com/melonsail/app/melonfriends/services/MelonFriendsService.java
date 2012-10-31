package com.melonsail.app.melonfriends.services;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.melonsail.app.melonfriends.sns.SnsOrg;
import com.melonsail.app.melonfriends.utils.Const;

@SuppressLint("HandlerLeak")
public class MelonFriendsService extends Service  {
	
	private static final String TAG = MelonFriendsService.class.getSimpleName();
	private static final int DEFAULT_INTERVAL = 15000;
	private static final int DEFAULT_DELAY = 3000;
	
	private NotificationManager mNM;
    private ArrayList<Messenger> mClients;
    private Messenger mIncomingMessenger;
    
    private Timer mTimer;
	private TimerTask mTimedTask;
	
	private SnsOrg mSnsOrg;
    
    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mClients = new ArrayList<Messenger>();
        mSnsOrg = new SnsOrg(this);
        mIncomingMessenger = new Messenger(new IncomingHandler());
        this.setTimer(DEFAULT_INTERVAL);
    }

	@Override
	public IBinder onBind(Intent arg0) {
		return mIncomingMessenger.getBinder();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mTimer != null) {
			mTimer.cancel();
		}
		Log.i(TAG, "Timer stopped.");
	}
	
	private void setTimer(int interval) {
		if(mTimer != null) {
			mTimer.cancel();
		}
		mTimer = new Timer();
		mTimedTask = new TimerTask() {
			@Override
			public void run() {
				Log.v(TAG, "Feed Retrieved Triggered! " );
				fSnsGetNewFeed();
			}
		};
		mTimer.scheduleAtFixedRate(mTimedTask, DEFAULT_DELAY, interval);
		Log.i(TAG, "Timer set to " + interval);
	}
	
	
	public void fSnsGetNewFeed() {
		mSnsOrg.fActiveSnsGetNewFeed(this);
		//notification on background feed retrieval complete
	}
	
	public void fSnsGetNewFeed(Message msg) {
		int snsIndex = msg.arg1;
		Log.i(TAG, "request index = " + snsIndex + " Active List = " + mSnsOrg.fGetActiveSnsList().size());
		mSnsOrg.fGetActiveSnsList().get(snsIndex).fGetNewsFeeds(this);
	}
	
	class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Const.MSG_CLIENT_REGISTER:
                    mClients.add(msg.replyTo);
                    Log.i(TAG, "Client Registered to Service");
                    break;
                case Const.MSG_CLIENT_UNREGISTER:
                    mClients.remove(msg.replyTo);
                    break;
                case Const.MSG_SERVICE_SET_REFRESH_INTERNAL:
                	setTimer(msg.arg1);
                    break;
                case Const.MSG_SERVICE_GET_NEWFEED:
                	Log.i(TAG, "Receive request to get new feed");
                	fSnsGetNewFeed(msg);
                	break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

	/**
	 * Reply message to messengers in the queue
	 * Remove dead messengers if error is captured
	 * @param snsName
	 * @param serviceMsg
	 */
	public void fReplyMesseage(String snsName, int serviceMsg) {
		for (int i=mClients.size()-1; i>=0; i--) {
            try {
                mClients.get(i).send(Message.obtain(null, serviceMsg, 1, 0));
                Log.i(TAG, "Reply client message: " + serviceMsg);
            } catch (RemoteException e) {
                mClients.remove(i);
            }
        }		
	}
}

package com.melonsail.app.melonfriends.activities;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.melonsail.app.melonfriends.controller.ServiceMessageListener;
import com.melonsail.app.melonfriends.services.MelonFriendsService;
import com.melonsail.app.melonfriends.utils.Const;
import com.melonsail.app.melonfriends.utils.Pref;

public class MelonFriendsServiceConnection {

	public static final String TAG = "ServiceConnection";
	private Messenger incomingMessenger;
	private Messenger outgoingMessenger;
	
	private ServiceMessageListener messageListener;
	
	//Members from Context
	private Activity mActivity;
	
	public MelonFriendsServiceConnection(Activity activity) {
		//messageListener = new
		this.mActivity = activity;
		
		this.incomingMessenger = new Messenger(new IncommingMsgHandler(messageListener));
	}
	
	public MelonFriendsServiceConnection(Activity activity, ServiceMessageListener listener) {
		this.mActivity = activity;
		this.messageListener = listener;
		
		this.incomingMessenger = new Messenger(new IncommingMsgHandler(listener));
	}
	
	class IncommingMsgHandler extends Handler {

		ServiceMessageListener listener;
		
		public IncommingMsgHandler(ServiceMessageListener listener) {
			this.listener = listener;
		}
		
		@Override
		public void handleMessage(Message msg) {
//			switch (msg.what) {
//				case Const.MSG_UI_RECEIVE_NEWFEED:
//					Log.i(TAG, "MSG_UI_RECEIVE_NEWFEED: Display UI");
//					String sns_pull2refresh = Pref.getMyStringPref(mActivity, Const.SNS_PULL_TO_REFRESH);
//					if ( sns_pull2refresh.length() > 0 ) {
//						((MainActivity)mActivity).fGetController().fRefreshContentView(sns_pull2refresh);
//					}
//					
//				break;
//			}
			Log.i(TAG, "Activity: " + mActivity.getLocalClassName() + " receive msg = " + msg);
			listener.handleMessage(msg.what);
		}
	}
	
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			outgoingMessenger = new Messenger(service);
			
			try {
	            Message msg = Message.obtain(null, Const.MSG_CLIENT_REGISTER);
	            msg.replyTo = incomingMessenger;
	            outgoingMessenger.send(msg);
	            Log.v(TAG, "service connected");
	        } catch (RemoteException e) {
	        	Log.v(TAG, "service crashed");
	        }
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			outgoingMessenger = null;
			Log.v(TAG, "service disconnected");
		}
	};
	
	
	public void fBindToService() {
		if(this.mActivity != null) {
			this.mActivity.bindService(new Intent(mActivity, MelonFriendsService.class), mConnection, Context.BIND_AUTO_CREATE);
		}
		else {
			Log.v(TAG, " Activity is not created ");
		}
	}
	
	public void fUnBindToService() {
		if(this.mActivity != null) {
			this.mActivity.unbindService(mConnection);
		}
	}
	
	public void fSendMessage(int msgWhat, int msgArg1, int msgArg2) {
		if(outgoingMessenger != null) {
			Message msg = Message.obtain(null, msgWhat, msgArg1, msgArg2);
			msg.replyTo = incomingMessenger;
			
			try {
				outgoingMessenger.send(msg);
				
			} catch (RemoteException e) {
				Log.v(TAG, " Message Send Error " + e.toString());
			}
		}
	}

	public boolean CheckSvcRunning() {

		final ActivityManager activityManager = (ActivityManager)mActivity.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals("com.melonsail.app.melonfriends.service.MelonFriendsService")){
                return true;
            }
        }
		return false;
	}
}

package com.melonsail.app.melonfriends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.melonsail.app.melonfriends.R;
import com.melonsail.app.melonfriends.controller.Controller;
import com.melonsail.app.melonfriends.controller.MainController;
import com.melonsail.app.melonfriends.services.MelonFriendsService;
import com.melonsail.app.melonfriends.utils.FlurryUtil;

public class MainActivity extends FragmentActivity {

	private MainController controller;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        controller = new MainController(this);
        controller.fInit();
        fAnalyseIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_item_ctx_menu, menu);
        return true;
    }
    
    @Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
    
    @Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
    
    @Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		fAnalyseIntent();
	}
    
    @Override
	protected void onStart() {
		super.onStart();
		controller.fBindService();
		FlurryUtil.onStart(this);
	}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		controller.onActivityResult(requestCode, resultCode, data);
	}
    
    @Override
	public void onBackPressed() {
    	controller.onBackPressed();
    }
    
    @Override
	protected void onStop() {
		super.onStop();
		controller.fUnBindService();
		FlurryUtil.onStop(this);
	}

    
    private void fAnalyseIntent() {
    	Bundle extras = getIntent().getExtras();
        if (extras == null) {
        	this.startService(new Intent(this, MelonFriendsService.class));
        } else {
        	// Display feed based on sns notification
        	controller.fAnaylseIntent(extras);
        }
    }
    
    public Controller fGetController() {
    	return controller;
    }
    
}

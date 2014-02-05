package com.dev.apps.devtwitterapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dev.apps.devtwitterapp.fragments.HomeTimelineFragment;
import com.dev.apps.devtwitterapp.fragments.MentionsFragment;
import com.dev.apps.devtwitterapp.fragments.TweetsListFragment;
import com.dev.apps.devtwitterapp.models.Tweet;
import com.dev.apps.devtwitterapp.models.User;



import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class TimelineActivity extends FragmentActivity implements TabListener {
	ListView lvTweets;
	ArrayList<Tweet> tweets;
	TweetsAdapter adapter;
	User me;
	ActionBar actionBar;
	TweetsListFragment fragmentTweets;
	TweetsListFragment home =new HomeTimelineFragment();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweets);
		actionBar = getActionBar();
		setupNavTab();
		//get users info for user id and profile pic
		//fragmentTweets = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentTweets);
		
		
		MyTwitterApp.getRestClient().getMyInfo(new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, JSONObject myInfo) {
				// TODO Auto-generated method stub
				//super.onSuccess(arg0, arg1);
				User.deleteMe();
				 me = User.fromJson(myInfo,true);
				//Log.d("DEBUG",me.getScreenName());
				actionBar.setTitle(Html.fromHtml("<small>@"+me.getScreenName()+"</small>"));
		
				//Log.d("DEBUG",""+statusCode);
				//Log.d("DEBUG",""+myInfo.toString());
			}
			
			
			@Override
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				//add persistance to load from db
				me = User.getMe();
				actionBar.setTitle(Html.fromHtml("<small>@"+me.getScreenName()+"</small>"));
				Log.d("DEBUG","FAIL TO GET MYINFO");
			}
		});
		
		

		

		

		
		
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	private void setupNavTab() {
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tabHome = actionBar.newTab().setText("Home").setTag("HomeTimelineFragment").setIcon(R.drawable.ic_home).setTabListener(this);
		Tab tabMention = actionBar.newTab().setText("Mentions").setTag("MentionsFragment").setIcon(R.drawable.ic_at).setTabListener(this);
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMention);
		actionBar.selectTab(tabHome);
		
	}
	
	public void clickPostTweet(MenuItem mi) {
    	//Toast.makeText(this, mi.toString(), Toast.LENGTH_SHORT).show();
		Intent i = new Intent(getApplicationContext(),ComposeTweet.class);
		i.putExtra("userid", me.getUserId());
		//i.putExtra("profilepic",me.getProfileImageUrl());
		startActivityForResult(i, 5);
		
    	//Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
		//startActivityForResult(i, 5);
		
	}
	
	public void refreshTweets(MenuItem mi) {
		MyTwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				// TODO Auto-generated method stub
				adapter.clear();
				adapter.addAll(Tweet.fromJson(jsonTweets));
				Toast.makeText(getBaseContext(),"Refreshed", Toast.LENGTH_SHORT).show();
			}
			
		});
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  	  // REQUEST_CODE is defined above
  	//Log.d("DEBUG","requestCode "+requestCode);
  	//Log.d("DEBUG","resultCode "+resultCode);
  	  if (resultCode == 100 && requestCode == 5) {
  	     // Extract name value from result extras
  	     Tweet mytweet = (Tweet) data.getSerializableExtra("mytweet");
  	     Log.d("DEBUG",mytweet.toString());
  	   //TweetsListFragment t = new TweetsListFragment();
  	     home.getAdapter().insert(mytweet,0);
  	     // Toast the name to display temporarily on screen
  	     //Toast.makeText(this, fil.getImageSize().toString(), Toast.LENGTH_SHORT).show();
  	  }
  	  if (resultCode == 50 && requestCode == 5) {

   	  }
  	}
	public void clickProfile(MenuItem mi ) {
		Intent i = new Intent(this,ProfileActivity.class);
		startActivity(i);
	}


	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		if(tab.getTag() == "HomeTimelineFragment") {
			fts.replace(R.id.frame_container, home);
		} else {
			fts.replace(R.id.frame_container, new MentionsFragment());
		}
		fts.commit();
	}
	




	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	

	

}

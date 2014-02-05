package com.dev.apps.devtwitterapp;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dev.apps.devtwitterapp.models.Tweet;
import com.dev.apps.devtwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ComposeTweet extends Activity {
	EditText etTweet;
	String id;
	String profilePic;
	TextView textView;
	TextView countView;
	ImageView imageView;
	Intent i = new Intent();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		Bundle extras = getIntent().getExtras();
		if (extras == null) {
		    //if no data, assume no network so load persisted data
			// to do add persistance
		    } else {
		    	User me = User.getData(extras.getLong("userid"));
		    	id = "@"+me.getScreenName();
		    	profilePic= me.getProfileImageUrl();
		    }
		
		textView = (TextView) findViewById(R.id.tvId);
		countView = (TextView) findViewById(R.id.tvCount);
		textView.setText(id);
		etTweet = (EditText) findViewById(R.id.etTweet);
		imageView =  (ImageView) findViewById(R.id.ivMyProfile);
		ImageLoader.getInstance().displayImage(profilePic,imageView);
		etTweet.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(s.length() > 120) {
					int left = 140 - s.length();
					//Toast.makeText(getBaseContext(),""+left, Toast.LENGTH_SHORT).show();
					//Log.d("DEBUG","length"+s.length());
					countView.setText(""+left);
				} else {
					countView.setText("");
				}
				
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		return true;
	}
	
	public void cancelTweet(View v) {
		setResult(50, i); 
		finish();
	}
	
	public void submitTweet(View v) {
		Boolean check1=false;
		Boolean check2=false;
		if(etTweet.getText().toString().isEmpty())  {
			Toast.makeText(getBaseContext(),"Please Add Text", Toast.LENGTH_SHORT).show();
			
		} else {
			check1=true;
		}
		if(etTweet.getText().toString().length() > 140) {
			Toast.makeText(getBaseContext(),"Tweet too long", Toast.LENGTH_SHORT).show();
			
		} else {
			check2=true;
		}
		if(check1 & check2) {
			String tweetString=etTweet.getText().toString();
			MyTwitterApp.getRestClient().postTweet(new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject tweetObj) {
					// TODO Auto-generated method stub
					Tweet myTweet = Tweet.fromJson(tweetObj);
					
					i.putExtra("mytweet",myTweet);
					
					setResult(100, i); 
					finish();
					
				}
			}, tweetString);
			}
		}
	}



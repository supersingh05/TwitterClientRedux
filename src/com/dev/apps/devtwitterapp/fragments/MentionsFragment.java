package com.dev.apps.devtwitterapp.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dev.apps.devtwitterapp.EndlessScrollListener;
import com.dev.apps.devtwitterapp.MyTwitterApp;
import com.dev.apps.devtwitterapp.models.Tweet;
import com.dev.apps.devtwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

public class MentionsFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyTwitterApp.getRestClient().getMentionsTimeline(new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(int a, JSONArray jsonTweets) {
				getAdapter().addAll(Tweet.fromJson(jsonTweets));
		        lvTweets.setOnScrollListener(new EndlessScrollListener() {
		    	    @Override
		    	    public void onLoadMore(int page, int totalItemsCount) {
		    	    	Log.d("DEBUG",""+totalItemsCount);
		    	    	Log.d("DEBUG","trying to add more messages");
		    	    	long maxid = tweets.get((totalItemsCount-1)).getTweetId();
		    	    	Log.d("DEBUG",tweets.get((totalItemsCount-1)).getBody());
		    	    	Log.d("DEBUG","maxid:"+maxid);
		    	    	//Log.d("DEBUG",""+tweets.get(aa).getId());
		    	    	MyTwitterApp.getRestClient().getOlderMentionsTimeline(new JsonHttpResponseHandler() {
		    	    		@Override
		    	    		public void onSuccess(JSONArray jsonTweets) {
		    	    				getAdapter().addAll(Tweet.fromJson(jsonTweets));
		    	    				
		    	    		}
		    				@Override
		    				public void onFailure(Throwable arg0) {
		    					// TODO Auto-generated method stub
		    					
		    					Toast.makeText(getActivity(),"No Internet Connection", Toast.LENGTH_SHORT).show();
		    					Log.d("DEBUG","FAIL TO LOAD MORE TWEETS");
		    				}
		    	    		
		    	    	}, maxid);
		    	    	
		    	    	
		    	    	
		    	    }
		            });
			}
			@Override
			public void onFailure(Throwable arg0) {
				Log.d("DEBUG","FAIL TO GET LOAD TWEETS");
			}
		});
				
			}
			

}

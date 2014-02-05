package com.dev.apps.devtwitterapp.fragments;

import org.json.JSONArray;

import com.dev.apps.devtwitterapp.EndlessScrollListener;
import com.dev.apps.devtwitterapp.MyTwitterApp;
import com.dev.apps.devtwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class HomeTimelineFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyTwitterApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(int a, JSONArray jsonTweets) {
				// tweets defined above
				getAdapter().addAll(Tweet.fromJson(jsonTweets));
				lvTweets.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapter, View parent, int position,
							long rowId) {
						Tweet tweet = tweets.get(position);
						Toast.makeText(getActivity(),""+tweet.getTweetId(), Toast.LENGTH_SHORT).show();
						
					}
				});
		        //endless scrolling
		        lvTweets.setOnScrollListener(new EndlessScrollListener() {
		    	    @Override
		    	    public void onLoadMore(int page, int totalItemsCount) {
		    	    	Log.d("DEBUG",""+totalItemsCount);
		    	    	Log.d("DEBUG","trying to add more messages");
		    	    	long maxid = tweets.get((totalItemsCount-1)).getTweetId();
		    	    	Log.d("DEBUG",tweets.get((totalItemsCount-1)).getBody());
		    	    	Log.d("DEBUG","maxid:"+maxid);
		    	    	//Log.d("DEBUG",""+tweets.get(aa).getId());
		    	    	MyTwitterApp.getRestClient().getOlderTimeline(new JsonHttpResponseHandler() {
		    	    		@Override
		    	    		public void onSuccess(JSONArray jsonTweets) {
		    	    			Log.d("DEBUG",jsonTweets.toString());
		    	    			Log.d("DEBUG","adding tweets size"+adapter.getCount());
		    	    				
		    	    				getAdapter().addAll(Tweet.fromJson(jsonTweets));
		    	    				Log.d("DEBUG","after size"+adapter.getCount());
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

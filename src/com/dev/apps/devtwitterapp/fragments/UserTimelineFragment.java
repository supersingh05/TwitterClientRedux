package com.dev.apps.devtwitterapp.fragments;

import org.json.JSONArray;
import org.json.JSONObject;




import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.apps.devtwitterapp.EndlessScrollListener;
import com.dev.apps.devtwitterapp.MyTwitterApp;
import com.dev.apps.devtwitterapp.R;
import com.dev.apps.devtwitterapp.models.Tweet;
import com.dev.apps.devtwitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserTimelineFragment extends TweetsListFragment {
	User me;
	//ActionBar actionBar;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
MyTwitterApp.getRestClient().getMyInfo(new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, JSONObject myInfo) {
				// TODO Auto-generated method stub
				//super.onSuccess(arg0, arg1);
				User.deleteMe();
				 me = User.fromJson(myInfo,true);
				//Log.d("DEBUG",me.getScreenName());
				// actionBar = getActivity().getActionBar();;
				//actionBar.setTitle(Html.fromHtml("<small>@"+me.getScreenName()+"</small>"));
				 populateProfile(me);

		
				//Log.d("DEBUG",""+statusCode);
				//Log.d("DEBUG",""+myInfo.toString());
			}
			
			
			@Override
			public void onFailure(Throwable arg0) {
				// TODO Auto-generated method stub
				//add persistance to load from db
				me = User.getMe();
				populateProfile(me);
				//actionBar.setTitle(Html.fromHtml("<small>@"+me.getScreenName()+"</small>"));
				Log.d("DEBUG","FAIL TO GET MYINFO");
			}
		});
		MyTwitterApp.getRestClient().getUserTimeline(new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(int a, JSONArray jsonTweets) {
				getAdapter().addAll(Tweet.fromJson(jsonTweets));
		        lvTweets.setOnScrollListener(new EndlessScrollListener() {
		    	    @Override
		    	    public void onLoadMore(int page, int totalItemsCount) {

		    	    	long maxid = tweets.get((totalItemsCount-1)).getTweetId();
		    	    	
		    	    	

		    	    	//Log.d("DEBUG",""+tweets.get(aa).getId());
		    	    	MyTwitterApp.getRestClient().getUserTimelineOlder(new JsonHttpResponseHandler() {
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
	private void populateProfile(User user) {
		TextView tvName = (TextView) getActivity().findViewById(R.id.tvName);
		TextView tvTagline = (TextView) getActivity().findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) getActivity().findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) getActivity().findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) getActivity().findViewById(R.id.ivProfileImage);
		tvName.setText(user.getName());
		tvFollowers.setText(user.getFollowersCount()+"followers");
		tvFollowing.setText(user.getFriendsCount()+"following");
		tvTagline.setText(user.getTagline());
		//Log.d("DEBUG",user.getTagline());
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(),ivProfileImage);
	
		
		
	}
}

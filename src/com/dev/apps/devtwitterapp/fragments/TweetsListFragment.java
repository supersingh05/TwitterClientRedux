package com.dev.apps.devtwitterapp.fragments;

//import android.R;
import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.dev.apps.devtwitterapp.EndlessScrollListener;
import com.dev.apps.devtwitterapp.MyTwitterApp;
import com.dev.apps.devtwitterapp.R;
import com.dev.apps.devtwitterapp.TweetsAdapter;
import com.dev.apps.devtwitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TweetsListFragment extends Fragment {
	ArrayList<Tweet> tweets;
	ListView lvTweets;
	TweetsAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inf.inflate(R.layout.fragment_tweets_list,parent,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		tweets = new ArrayList<Tweet>();
		lvTweets = (ListView) getActivity().findViewById(R.id.lvTweets); 
		adapter =  new TweetsAdapter(getActivity(),tweets);
		lvTweets.setAdapter(adapter);

	}
	
	public TweetsAdapter getAdapter() {
		return adapter;
	}
}

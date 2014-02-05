package com.dev.apps.devtwitterapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Table(name= "Tweet")
public class Tweet extends Model implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="body")
	private String body;
	@Column(name="uid",unique=true,onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	@Column(name="favorited")
	private boolean favorited;
	@Column(name="retweeted")
	private boolean retweeted;
	@Column(name="user")
    private User user;
	@Column(name="created_at")
	private String created_at;
	
	
    public Tweet(){
        super();
    }
    
    
    public static List<Tweet> getData() {
    	return new Select().from(Tweet.class).orderBy("created_at DESC").execute();
    }
    
 
    
    public String getDate() {
        return created_at;
    }
    
    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public long getTweetId() {
        return uid;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
        	
        	try {
        		//Log.d("DEBUG","trying date thing");
        		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
				java.util.Date d = sdf.parse(jsonObject.getString("created_at").toString());
				sdf.applyPattern("y-M-d H:m:s");
				String newDateString = sdf.format(d);
				//Log.d("DEBUG","datestring"+newDateString);
				tweet.created_at=newDateString;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				Log.d("DEBUG",e.toString());
			}
        	 
        	
        	tweet.body = jsonObject.getString("text");
        	//Log.d("DEBUG",tweet.body);
        	//Log.d("DEBUG","????????");
        	tweet.uid = jsonObject.getLong("id");
        	tweet.favorited = jsonObject.getBoolean("favorited");
        	tweet.retweeted = jsonObject.getBoolean("retweeted");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
            tweet.save();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        return tweets;
    }
}

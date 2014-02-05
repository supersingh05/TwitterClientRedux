package com.dev.apps.devtwitterapp.models;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "User")
public class User extends Model implements Serializable {
	@Column(name = "Name")
	private String name;
	@Column(name = "uid")
	private long uid;
	@Column(name = "screenName")
	private String screenName;
	@Column(name = "profileBgImageUrl")
	private String profileBgImageUrl;
	@Column(name = "profileImageUrl")
	private String profileImageUrl;
	@Column(name = "numTweets")
	private int numTweets;
	@Column(name = "followerCount")
	private int followersCount;
	@Column(name = "friendsCount")
	private int friendsCount;
	@Column(name="me")
	private boolean me;
	@Column(name="tagline")
	private String tagline;
	
    public User(){
        super();
    }
    public String getName() {
        return name;
    }
    public String getTagline() {
        return tagline;
    }

    public Long getUserId() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBgImageUrl;
    }
    
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public int getNumTweets() {
        return numTweets;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }
    
    public static void deleteMe() {
    	new Delete().from(User.class).where("me = ?", true).execute();
    }
    
    public static User getMe() {
    	return new Select().from(User.class).where("me = ?", true).executeSingle();
    }
    
    public static User getData(long uid) {
    	return new Select().from(User.class).where("uid=?", uid).executeSingle();
    }

    public static User fromJson(JSONObject json) {
        User u = new User();
        try {
        	u.name = json.getString("name");
        	u.uid = json.getLong("id");
        	u.screenName = json.getString("screen_name");
        	u.profileBgImageUrl = json.getString("profile_background_image_url");
        	u.profileImageUrl = json.getString("profile_image_url");
        	u.numTweets = json.getInt("statuses_count");
        	u.followersCount = json.getInt("followers_count");
        	u.friendsCount = json.getInt("friends_count");
        	u.tagline= json.getString("description");
        	u.me=false;
        	u.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }
    
    public static User fromJson(JSONObject json, boolean me) {
        User u = new User();
        try {
        	u.name = json.getString("name");
        	u.uid = json.getLong("id");
        	u.screenName = json.getString("screen_name");
        	u.profileBgImageUrl = json.getString("profile_background_image_url");
        	u.profileImageUrl = json.getString("profile_image_url");
        	u.numTweets = json.getInt("statuses_count");
        	u.followersCount = json.getInt("followers_count");
        	u.friendsCount = json.getInt("friends_count");
        	u.tagline= json.getString("description");
        	u.me = true;
        	u.save();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }


}

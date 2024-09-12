package com.mobitech.speachtotext.textToSpeech.sharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mobitech.speachtotext.textToSpeech.constants.Constants;
import com.mobitech.speachtotext.textToSpeech.model.RecordedFileModel;
import com.mobitech.speachtotext.textToSpeech.model.UserModel;

import java.util.UUID;

import static com.mobitech.speachtotext.textToSpeech.constants.Constants.SINGLE_AUDIO_ITEM;

public class UserSettings {

    private static final String SHARED_PREF_NAME = "user_settings";

    private static UserSettings sSharedPrefs;
    private final SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private boolean mBulkUpdate = false;
    private Context mContextRef;

    public UserSettings(Context context) {
        mPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        mContextRef = context;
    }

    public void createUserSession(UserModel userModel)
    {
        doEdit();
        mEditor.putBoolean(Constants.LOGIN, true);
        Gson gson = new Gson();
        mEditor.putString("UserModel",gson.toJson(userModel));
        mEditor.apply();
        doCommit();
    }

    public UserModel getUserSession(){
        Gson gson = new Gson();
        String json = mPref.getString("UserModel", "");
        return gson.fromJson(json, UserModel.class);
    }
    public void createRecordedFileObj(RecordedFileModel recordedFileModel)
    {
        doEdit();
//        mEditor.putBoolean(AppConstants.LOGIN, true);
        Gson gson = new Gson();
        mEditor.putString(SINGLE_AUDIO_ITEM,gson.toJson(recordedFileModel));
        mEditor.apply();
        doCommit();
    }
    public RecordedFileModel getRecordedFileObj(){
        Gson gson = new Gson();
        String json = mPref.getString(SINGLE_AUDIO_ITEM, "");
        return gson.fromJson(json, RecordedFileModel.class);
    }
    public static UserSettings getInstance(Context context) {
        if (sSharedPrefs == null) {
            sSharedPrefs = new UserSettings(context.getApplicationContext());
        }
        return sSharedPrefs;
    }

    public static UserSettings getInstance() {
        if (sSharedPrefs != null) {
            return sSharedPrefs;
        }

        //Option 1:
        throw new IllegalArgumentException("Should use getInstance(Context) at least once before using this method.");

        //Option 2:
        // Alternatively, you can create a new instance here
        // with something like this:
        // getInstance(MyCustomApplication.getAppContext());
    }

//    public String getUUID() {
//        String uuid = mPref.getString("UUID", null);
//        if (uuid == null) {
//            uuid = UUID.randomUUID().toString();
//            set(getKeyFor("UUID", 0), uuid);
//        }
//        return uuid;
//    }

    public void set(String key, String val) {
        doEdit();
        mEditor.putString(key, val);
        doCommit();
    }

    public void set(String key, int val) {
        doEdit();
        mEditor.putInt(key, val);
        doCommit();
    }

    public void set(String key, boolean val) {
        doEdit();
        mEditor.putBoolean(key, val);
        doCommit();
    }

    public void set(String key, float val) {
        doEdit();
        mEditor.putFloat(key, val);
        doCommit();
    }

    public void set(String key, double val) {
        doEdit();
        mEditor.putString(key, String.valueOf(val));
        doCommit();
    }

    public void set(String key, long val) {
        doEdit();
        mEditor.putLong(key, val);
        doCommit();
    }

    public boolean containValue(String Key) {
        return mPref.contains(Key);
    }

    public void deletePreferencesForKey(String key) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove(key);
        editor.commit();
    }

    public void clearAllPreferences() {
        SharedPreferences.Editor editor = mPref.edit();
        editor.clear();
        editor.commit();
    }

    public String getString(String key) {
        return mPref.getString(key, null);
    }

    public int getInt(String key) {
        return mPref.getInt(key, 0);
    }

    public long getLong(String key) {
        return mPref.getLong(key, 0);
    }

    public float getFloat(String key) {
        return mPref.getFloat(key, 0);
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public boolean getBoolean(String key) {
        return mPref.getBoolean(key, false);
    }

    public String getString(String key, String defaultValue) {
        return mPref.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return mPref.getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return mPref.getLong(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return mPref.getFloat(key, defaultValue);
    }

    public double getDouble(String key, double defaultValue) {
        try {
            return Double.valueOf(mPref.getString(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPref.getBoolean(key, defaultValue);
    }

    /**
     * Remove keys from SharedPreferences.
     *
     * @param keys The name of the key(s) to be removed.
     */
    public void remove(String... keys) {
        doEdit();
        for (String key : keys) {
            mEditor.remove(key);
        }
        doCommit();
    }

    /**
     * Remove all keys from SharedPreferences.
     */
    public void clear() {
        doEdit();
        mEditor.clear();
        doCommit();
    }

    public void edit() {
        mBulkUpdate = true;
        mEditor = mPref.edit();
    }

    public void commit() {
        mBulkUpdate = false;
        mEditor.commit();
        mEditor = null;
    }

    private void doEdit() {
        if (!mBulkUpdate && mEditor == null) {
            mEditor = mPref.edit();
        }
    }

    private void doCommit() {
        if (!mBulkUpdate && mEditor != null) {
            mEditor.commit();
            mEditor = null;
        }
    }

    public String getKeyFor(String key, int index) {
        return String.format(key, index);
    }



}






package ml.uncoded.yts.searchyoutube;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class SharedPrefrenceUser {

    private static final String USER_Login_SHARED_PREFRENCE = "user";

    private static final String KEY_PHONE = "keyphone";


    private static final String KEY_ISNEWUSER = "keyisnewuser";
    private static SharedPrefrenceUser sharedPrefrence_User_instance;
    private static Context context_to_use;


    public SharedPrefrenceUser(Context context) {
        context_to_use = context;
    }

    public static synchronized SharedPrefrenceUser getInstance(Context context) {
        if (sharedPrefrence_User_instance == null) {
            sharedPrefrence_User_instance = new SharedPrefrenceUser(context);
        }
        return sharedPrefrence_User_instance;
    }


    public static void setKeyIsnewuser(String phoneNo) {
        SharedPreferences mSharedPrefrences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPrefrences.edit();
        editor.putString(KEY_ISNEWUSER, "true");
        editor.putString(KEY_PHONE,phoneNo);
        editor.apply();

    }


    public String getKeyIsnewuser() {
        SharedPreferences mSharedPreferences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(KEY_ISNEWUSER, "");
    }


    public String getKeyPhone() {
        SharedPreferences mSharedPreferences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        return mSharedPreferences.getString(KEY_PHONE, " ");
    }







    public void logout() {
//        try{
//            Realm r=Realm.getDefaultInstance();
//            if(!r.isEmpty() && !r.isInTransaction()){
//                r.beginTransaction();
//                r.deleteAll();
//                r.commitTransaction();}}catch (RealmException re){re.printStackTrace();}
        SharedPreferences sharedPreferences = context_to_use.getSharedPreferences(USER_Login_SHARED_PREFRENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(context_to_use, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context_to_use.startActivity(intent);

    }
}
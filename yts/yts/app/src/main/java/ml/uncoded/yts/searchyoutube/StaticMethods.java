package ml.uncoded.yts.searchyoutube;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;

public class StaticMethods {

    public static boolean checkInternetConnectivity(Context c){

        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);
            mobileDataEnabled = (Boolean)method.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WifiManager wifi = (WifiManager)c.getSystemService(Context.WIFI_SERVICE);
            if(!(wifi.isWifiEnabled() || mobileDataEnabled)) {
                String str="Please Enable Internet";
                Dialogs ds=new Dialogs(str);
                ds.show(((FragmentActivity)c).getSupportFragmentManager(),"Hello");
                return false;
            }
            return true; //Internet is On
        }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);

        if (inputMethodManager!=null && inputMethodManager.isActive() && inputMethodManager.isAcceptingText() ) {
            inputMethodManager.hideSoftInputFromWindow(
                    new View(activity).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showSoftKeyboard(Activity activity,View view){
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view,0);
    }

    public static void  permissionmethod(Context c){
        int Permission_All = 1;

        String[] Permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MEDIA_CONTENT_CONTROL,
                Manifest.permission.CAMERA, };
        if(!hasPermissions(c, Permissions)){
            ActivityCompat.requestPermissions((Activity) c, Permissions, 1);
        }

    }

    public static boolean hasPermissions(Context context, String... permissions){

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && context!=null && permissions!=null){
            for(String permission: permissions){
                if(ActivityCompat.checkSelfPermission(context, permission)!= PackageManager.PERMISSION_GRANTED){
                    return  false;
                }
            }
        }
        return true;
    }

    public static HashSet<String> getAllLabels(){

        String[] MyLabelArray = {
                "COMPUTER","LAPTOP","NETBOOK","TECHNOLOGY",};
       return new HashSet<>((Arrays.asList(MyLabelArray)));
    }



}




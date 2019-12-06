package ml.uncoded.yts.searchyoutube;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;



public class OtpScreenFragment extends Fragment {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    public View mPhoneEdit;
    public EditText mOtpEdt;
    private Button mVerifyBtn;
    private String mOtpNumber,codeSent;
    private ProgressBar mProgresBar;
     TextView tvMobNo;

    FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_otp_screen, container, false);
        mOtpEdt = (EditText) v.findViewById(R.id.otp_input);
        mVerifyBtn = (Button) v.findViewById(R.id.btn_input_upload);
        mProgresBar = (ProgressBar) v.findViewById(R.id.otp_progressbar);
        tvMobNo=v.findViewById(R.id.mobileEditText);
        tvMobNo.setText(getArguments().getString("KEY_PHONE_NO"));
        mPhoneEdit = v.findViewById(R.id.mobileNoEdit);
        mAuth=FirebaseAuth.getInstance();
        codeSent=getArguments().getString("KEY_CODE_SENT");
        initListners();

        if (checkAndRequestPermissions()) {
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("otp"));
        }

       // resendCountDownCounter mCount = new resendCountDownCounter(30000,1000,mTimerText,mResendOtp,getActivity());
        //mCount.start();
        return v;

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                String message = intent.getStringExtra("message");
                if(message.toLowerCase().contains("marg sahayak")) {
                    String[] temp = message.split(" ");
                    message = temp[temp.length-1];
                    mOtpNumber = message;
                    mOtpEdt.setText(message);
                    checkOtp();
                }
            }
        }
    };


    private void initListners() {
        mOtpEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    checkOtp();
                    return true;
                }
                return false;
            }
        });

        mPhoneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction mFragmentTransition = getActivity().getSupportFragmentManager().beginTransaction();
                mFragmentTransition.replace(R.id.frame_layout, new LoginFragment());
                mFragmentTransition.addToBackStack(null);
                mFragmentTransition.commit();
            }
        });
        mVerifyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                checkOtp();
            }
        });
    }


    private void checkOtp() {

        mOtpNumber = mOtpEdt.getText().toString();
        mOtpEdt.setError(null);

        boolean cancel = false;

        if (TextUtils.isEmpty(mOtpNumber)) {
            mOtpEdt.setError(getString(R.string.error_required_field));
            cancel = true;
        } else if (!(Pattern.matches("[0-9]+", mOtpNumber))) {
            mOtpEdt.setError(getString(R.string.error_OTP_size_invalid));
            cancel = true;
        }

        if (cancel) {
            mOtpEdt.requestFocus();
        } else {

            if (StaticMethods.checkInternetConnectivity(getActivity())) {

                mOtpEdt.setEnabled(false);
                mVerifyBtn.setEnabled(false);
                mVerifyBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SCREEN);
                StaticMethods.hideSoftKeyboard(getActivity());
                mProgresBar.setVisibility(View.VISIBLE);

                PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(codeSent,mOtpNumber);
                mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //here you can open new activity
                            Toast.makeText(getActivity(),
                                    "Login Successfull", Toast.LENGTH_LONG).show();

                          SharedPrefrenceUser.setKeyIsnewuser(tvMobNo.getText().toString());
                           startActivity(new Intent(getActivity(),ImgCaptureActivity.class));
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getActivity(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });






            }
        }

    }


    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void onResume() {

//        if(!getActivity().isDestroyed()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                IntentFilter i = new IntentFilter();
//                i.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
//                i.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
//                getActivity().registerReceiver(receiver, i);
//            }


            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }
}

package ml.uncoded.yts.searchyoutube;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment {

    private EditText mPhoneView;
    private ProgressBar mProgresBar;
    private Button mNextBtn;
    String codeSent;
    String TAG = "LoginFragment";
    FirebaseAuth mAuth;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mPhoneView = (EditText) v.findViewById(R.id.login_input);
        mNextBtn = (Button) v.findViewById(R.id.btn_login);
        mProgresBar = (ProgressBar) v.findViewById(R.id.login_progressbar);
        initListners();
        mPhoneView.requestFocus();
        return v;

    }

    private void initListners() {
        mPhoneView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        mNextBtn.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {

        mPhoneView.setError(null);
        final String phoneNumber = mPhoneView.getText().toString();

        boolean cancel = false;

        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneView.setError(getString(R.string.error_required_field));
            cancel = true;
        } else if (!(Pattern.matches("[0-9]+", phoneNumber) && (phoneNumber.length() == 10))) {
            mPhoneView.setError(getString(R.string.error_number_invalid));
            cancel = true;
        }

        if (cancel) {
            //Phone Number is not OK show it in focus..
            mPhoneView.requestFocus();
        } else {


            if (StaticMethods.checkInternetConnectivity(getActivity())) {

                mPhoneView.setEnabled(false);

                mNextBtn.setEnabled(false);
                mNextBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SCREEN);
                StaticMethods.hideSoftKeyboard(getActivity());
                mProgresBar.setVisibility(View.VISIBLE);
                Log.d(TAG, "attemptLogin: ");
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91"+phoneNumber,
                        30,
                        TimeUnit.SECONDS,
                        getActivity(),
                        mCallbacks);

            }

        }


    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential.getSmsCode());
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.d(TAG, "onVerificationFailed: " + e.getMessage());
            Dialogs dialogs=new Dialogs(e.getMessage());
            dialogs.show(getFragmentManager(),"ErrMsg");
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.d(TAG, "onCodeSent: " + s);
            codeSent = s;
            FragmentManager fm = getFragmentManager();

            OtpScreenFragment otpScreenFragment = new OtpScreenFragment();
            Bundle bundle = new Bundle();
            bundle.putString("KEY_PHONE_NO", mPhoneView.getText().toString());
            bundle.putString("KEY_CODE_SENT", codeSent);
            otpScreenFragment.setArguments(bundle);
            if (fm.findFragmentById(R.id.login_fragment) != null)
                fm.beginTransaction().remove(fm.findFragmentById(R.id.login_fragment));

            fm.beginTransaction()
                    .replace(R.id.frame_layout, otpScreenFragment)
                    .commit();
        }
    };

}

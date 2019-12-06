package ml.uncoded.yts.searchyoutube;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Dialogs extends DialogFragment {
    TextView textView;
    Button btnOk;
    View.OnClickListener clickListener;
    public String strErrMsg;
    public Dialogs(){}
    @SuppressLint("ValidFragment")
    public Dialogs(String s, View.OnClickListener clickListener){
        this.clickListener=clickListener;
        strErrMsg=s;
    }
    @SuppressLint("ValidFragment")
    public Dialogs(String s){
        strErrMsg=s;
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.custom_dialog_layout, container, false);
        textView=(TextView)view.findViewById(R.id.edt_ip);
        btnOk =(Button) view.findViewById(R.id.btn_Ok);
        textView.setText(strErrMsg);
        if(clickListener==null){
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        else{

            btnOk.setOnClickListener(clickListener);

        }
setCancelable(false);

        return view;
    }

    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {

//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
//            setStyle(STYLE_NORMAL,android.R.style.Theme_Material_Dialog_Alert);
//        }
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }



}
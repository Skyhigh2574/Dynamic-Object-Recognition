package ml.uncoded.yts.searchyoutube;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TagActivity extends AppCompatActivity {

    String TAG="TagActivity";
    mDataAdapter adapter;
    ListView listView;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        listView=findViewById(R.id.listview);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
       myRef = database.getReference("Tag");
        readData();
    }

    private void readData() {
        Log.d(TAG, "readData: calling");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                TagModel tagModel=dataSnapshot.getValue(TagModel.class);
                ArrayList<String> tags=tagModel.getTagList();
                adapter=new mDataAdapter(TagActivity.this,R.layout.tag_item,tags);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    readData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    readData();
    }

}

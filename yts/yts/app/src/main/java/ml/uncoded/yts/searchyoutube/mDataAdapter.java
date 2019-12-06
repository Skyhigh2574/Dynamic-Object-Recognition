package ml.uncoded.yts.searchyoutube;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;


public class mDataAdapter extends ArrayAdapter<String> {

    Context context;
    int resource;
    List<String> tags;
    public mDataAdapter(Context context, int resource, List<String> tags) {
        super(context,resource,tags);
        this.context=context;
        this.resource=resource;
        this.tags=tags;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(resource,null,false);
        final TextView tag=view.findViewById(R.id.tv_tag);
        tag.setText(tags.get(position));
        Log.d("tagData", "Tag: "+tag.getText());
        tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, YoutubeResultActivity.class);
                i.putExtra("Search_String",tag.getText());
                context.startActivity(i);
            }
        });
        return view;

    }
}

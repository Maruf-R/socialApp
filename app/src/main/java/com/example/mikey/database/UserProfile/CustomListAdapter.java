package com.example.mikey.database.UserProfile;

import android.app.Activity;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

import com.example.mikey.database.R;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> itemname;
    private final ArrayList<Integer> imgid;
    private final ArrayList<String> age;

    public CustomListAdapter(Activity context, ArrayList<String> itemname, ArrayList<Integer> imgid, ArrayList<String> age ) {
        super(context, R.layout.activity_favourites, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.age = age;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.activity_favourites, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView extratxt = (TextView) rowView.findViewById(R.id.textView1);



        txtTitle.setText(itemname.get(position));
        imageView.setImageResource(imgid.get(position));
        extratxt.setText(age.get(position));
        return rowView;

    };
}
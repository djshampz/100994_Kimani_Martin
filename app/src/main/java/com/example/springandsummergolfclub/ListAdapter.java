package com.example.springandsummergolfclub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<GolfClub> {


    @SuppressLint("ResourceType")
    public ListAdapter(Context context, ArrayList<GolfClub> golfClubArrayList){

        super(context, R.layout.list_view,R.id.hidden_content, golfClubArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        GolfClub golfClub = getItem(position);


        if(convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent,false);
        }

        ImageView imageView = convertView.findViewById(R.id.prof_pic);
        TextView golfClubName = convertView.findViewById(R.id.golf_club_name);
        TextView golfClubDescription = convertView.findViewById(R.id.golf_club_description);


        imageView.setImageResource(golfClub.imageId);
        golfClubDescription.setText(golfClub.description);
        golfClubName.setText(golfClub.names);

        return super.getView(position, convertView, parent);
    }
}

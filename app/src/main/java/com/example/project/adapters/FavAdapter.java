package com.example.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.favourites.FavItem;
import com.example.project.favourites.FavViewModel;
import com.example.project.utilities.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {


    private Context context;
    private List<FavItem> favItems=new ArrayList<>();//otherwise it will be null and any method called results in NPE
    private FavViewModel favViewModel;

    public FavAdapter(FavViewModel favViewModel){
        this.favViewModel=favViewModel;

    }

    public void updateList(List<FavItem> favItems) {
        this.favItems = favItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row_fav, parent, false);
        return new FavAdapter.ViewHolder(view,favViewModel);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        FavItem favItem=favItems.get(position);
        holder.title.setText(favItem.getTitle());
       holder.publishedAt.setText(Utils.DateFormat(favItem.getPublishedAt()));
        try {
            holder.time.setText((" \u2022 " + Utils.DateToTimeFormat(favItem.getPublishedAt())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
      Picasso.get().load(favItem.getUrlToImage()).into(holder.mImageView, new Callback() {
          @Override
          public void onSuccess() {

              holder.progressBar.setVisibility(View.GONE);
          }

          @Override
          public void onError(Exception e) {
              Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();

          }
      });

    }

    @Override
    public int getItemCount() {
        return favItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public Button fav_btn;
        public TextView title,publishedAt,time;
        ProgressBar progressBar;
        FavViewModel favViewModel;

        public ViewHolder(@NonNull View itemView, final FavViewModel favViewModel) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.image_view_fav);

            publishedAt=itemView.findViewById(R.id.publishedAtFav);
            title=itemView.findViewById(R.id.titleFav);
            time=itemView.findViewById(R.id.timeFav);
            this.favViewModel=favViewModel;

            progressBar=itemView.findViewById(R.id.progress_load_photo_fav);
            fav_btn=itemView.findViewById(R.id.fav_btn_fav);
            fav_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

        }
    }
}

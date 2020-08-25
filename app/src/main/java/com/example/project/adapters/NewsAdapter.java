package com.example.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.favourites.FavItem;
import com.example.project.favourites.FavRepository;
import com.example.project.favourites.FavViewModel;
import com.example.project.fragments.Fragment_News_Fav;
import com.example.project.model.Article;
import com.example.project.utilities.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private OnItemClickListener mListener;
    FavViewModel fabVM;



    private Context context;
    private List<Article> articleList;

    public NewsAdapter(Context context, List<Article> blogList,OnItemClickListener listener,FavViewModel fabVM) {
        this.context = context;
        this.articleList = blogList;
        mListener=listener;
       this.fabVM=fabVM;
    }

    public void updateList(List<Article> blogList){
        this.articleList=blogList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row_home, parent, false);
        return new ViewHolder(view, context,mListener,fabVM);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
          Article article=articleList.get(position);
          holder.title.setText(article.getTitle());
          holder.publishedAt.setText(Utils.DateFormat(article.getPublishedAt()));
        try {
            holder.time.setText((" \u2022 " + Utils.DateToTimeFormat(article.getPublishedAt())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Picasso.get().load(article.getUrlToImage()).into(holder.mImageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        OnItemClickListener mListener;
        public ImageView mImageView;
        public Button fav_btn;
        public TextView title,publishedAt,time;
        ProgressBar progressBar;
        FavViewModel fabVM;


        public ViewHolder(@NonNull View itemView, final Context ctx, OnItemClickListener mListener, final FavViewModel fabVM) {
            super(itemView);
            context=ctx;
            this.mListener=mListener;
            this.fabVM=fabVM;


            mImageView=itemView.findViewById(R.id.image_view);

            publishedAt=itemView.findViewById(R.id.publishedAt);
            title=itemView.findViewById(R.id.title);
            time=itemView.findViewById(R.id.time);

            progressBar=itemView.findViewById(R.id.progress_load_photo);
            fav_btn=itemView.findViewById(R.id.fav_btn);
            itemView.setOnClickListener(this);
            fav_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Article article=articleList.get(getAdapterPosition());
                    String title= article.getTitle();
                    String publishedAt=article.getPublishedAt();
                    String UrlToImage=article.getUrlToImage();
                    String Url=article.getUrl();
                    FavItem fav=new FavItem(title,publishedAt,UrlToImage,Url);
                    fabVM.insert(fav);
                    Toast.makeText(ctx, "Added to favourites!", Toast.LENGTH_SHORT).show();

                }
            });
        }

        @Override
        public void onClick(View view) {
                 mListener.onItemClick(getAdapterPosition());
        }

    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}

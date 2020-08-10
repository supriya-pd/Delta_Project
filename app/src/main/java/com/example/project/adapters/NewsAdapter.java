package com.example.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.model.Article;
import com.example.project.utilities.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {


    private Context context;
    private List<Article> articleList;

   /* private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }*/

    public NewsAdapter(Context context, List<Article> blogList) {
        this.context = context;
        this.articleList = blogList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row_home, parent, false);
        return new ViewHolder(view, context/*,mListener*/);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImageView;
        public ToggleButton fav_btn;
        public TextView title,publishedAt,time;
        ProgressBar progressBar;


        public ViewHolder(@NonNull View itemView,Context ctx/*,final OnItemClickListener
                listener*/) {
            super(itemView);
            context=ctx;
            mImageView=itemView.findViewById(R.id.image_view);

            publishedAt=itemView.findViewById(R.id.publishedAt);
            title=itemView.findViewById(R.id.title);
            time=itemView.findViewById(R.id.time);

            progressBar=itemView.findViewById(R.id.progress_load_photo);
            fav_btn=itemView.findViewById(R.id.fav_btn);

           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });*/


        }
    }
}

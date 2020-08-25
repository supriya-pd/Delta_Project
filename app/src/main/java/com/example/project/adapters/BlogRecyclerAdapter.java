package com.example.project.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.activities.EditPostActivity;
import com.example.project.model.Article;
import com.example.project.model.Blog;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder>{

    private Context context;
    private List<Blog> blogList;
    DatabaseReference reference;
    FirebaseUser mUser;


    public BlogRecyclerAdapter(Context context, List<Blog> blogList, DatabaseReference reference, FirebaseUser mUser) {
        this.context = context;
        this.blogList = blogList;
        this.reference=reference;
        this.mUser=mUser;

    }


    public void updateList(List<Blog> blogList){
        this.blogList=blogList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_row_posts, parent, false);
        return new ViewHolder(view, context,reference,mUser,blogList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Blog blog = blogList.get(position);
        String imageUrl = null;

        holder.title.setText(blog.getTitle());
        holder.desc.setText(blog.getDesc());


        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(Long.valueOf(blog.getTimestamp())).getTime());
        String print=blog.getTimestamp();

        holder.timestamp.setText(formattedDate);
        imageUrl = blog.getImage();

        Picasso.get()
                .load(imageUrl)
                .into(holder.image);



    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView desc;
        public TextView timestamp;
        public ImageView image;
        String userId;
        Button deleteBTN,editBTN;
        DatabaseReference reference;
          final FirebaseUser mUser;
         final List<Blog> blogList;



        public ViewHolder(@NonNull View itemView, Context ctx, final DatabaseReference reference, final FirebaseUser mUser, final List<Blog> blogList) {
            super(itemView);
            context = ctx;
            this.reference=reference;
            this.mUser=mUser;
            this.blogList=blogList;


            title = (TextView) itemView.findViewById(R.id.postTitleList);
            desc = (TextView) itemView.findViewById(R.id.postTextList);
            image = (ImageView) itemView.findViewById(R.id.postImageList);
            timestamp = (TextView) itemView.findViewById(R.id.timestampList);
            deleteBTN=(Button)itemView.findViewById(R.id.deleteBTN);
            editBTN=(Button) itemView.findViewById(R.id.editBTN);
            userId = null;

            final int position = getAdapterPosition()+1;
            Blog blogDeleteUpdate=new Blog();
            if(blogList!=null) {

                blogDeleteUpdate= blogList.get(position);

            }


            final String del = blogDeleteUpdate.getKey();

            final Blog finalBlogDeleteUpdate = blogDeleteUpdate;
            deleteBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    if(mUser!=null){
                        if(mUser.getEmail().equalsIgnoreCase(finalBlogDeleteUpdate.getUsername())) {
                            final DatabaseReference referenceChild=reference.child(del);
                            reference.removeValue();
                            blogList.remove(position);
                            notifyItemRemoved(position);

                        }else{
                            Toast.makeText(context, "You can't delete this post!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(context, "You are not logged in!", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            final Blog finalBlogDeleteUpdate1 = blogDeleteUpdate;
            editBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(mUser!=null) {
                        if (mUser.getEmail().equalsIgnoreCase(finalBlogDeleteUpdate.getUsername())) {
                            final String title = finalBlogDeleteUpdate1.getTitle();
                            final String desc = finalBlogDeleteUpdate1.getDesc();
                            Intent intent = new Intent(context, EditPostActivity.class);
                            intent.putExtra("key", del);
                            intent.putExtra("title", title);
                            intent.putExtra("desc", desc);
                           // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }else{
                            Toast.makeText(context, "You can't edit this post!", Toast.LENGTH_SHORT).show();
                        }



                    }else{
                        Toast.makeText(context, "You are not logged in!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

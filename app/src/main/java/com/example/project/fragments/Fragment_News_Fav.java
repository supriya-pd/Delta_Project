package com.example.project.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapters.FavAdapter;
import com.example.project.favourites.FavItem;
import com.example.project.favourites.FavViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class Fragment_News_Fav extends Fragment {
    FavViewModel favViewModel;
    RecyclerView recyclerView;
    FavAdapter favAdapter;
    List<FavItem> favItemList;


     public Fragment_News_Fav(){}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_fav,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favItemList=new ArrayList<>();

        favViewModel= new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(FavViewModel.class);

         recyclerView =(RecyclerView) view.findViewById(R.id.recyclerViewFav);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
       favAdapter= new FavAdapter(favViewModel);
        recyclerView.setAdapter(favAdapter);
        ItemTouchHelper helper=new ItemTouchHelper(simple);
        helper.attachToRecyclerView(recyclerView);



        favViewModel.getAllFav().observe(this, new Observer<List<FavItem>>() {
            @Override
            public void onChanged(List<FavItem> favItems) {
                 //update recycler view
                //Toast.makeText(getContext(), "OnChanged", Toast.LENGTH_SHORT).show();
                favAdapter.updateList(favItems);
                favItemList=favItems;

            }
        });
     }

    ItemTouchHelper.SimpleCallback simple = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position=viewHolder.getAdapterPosition();
            final FavItem favDelete=favItemList.get(position);
            favViewModel.delete(favDelete);

            Snackbar.make(recyclerView, "Don't want to delete?", BaseTransientBottomBar.LENGTH_LONG).
                    setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           favViewModel.insert(favDelete);
                        }
                    }).show();



        }
    };
}

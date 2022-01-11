package com.ggenius.whattowear.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ggenius.whattowear.R;
import com.ggenius.whattowear.Settings;
import com.ggenius.whattowear.model.Clothes;

import java.util.List;

public class ClothesAdapter extends RecyclerView.Adapter<ClothesAdapter.ClothesViewHolder> {

    Context context;
    List<Clothes> clothes;

    public ClothesAdapter(Context context, List<Clothes> clothes) {
        this.context = context;
        this.clothes = clothes;
    }

    @NonNull
    @Override
    public ClothesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View clothesItems = LayoutInflater.from(context).inflate(R.layout.clothes_item, parent, false);
        return new ClothesViewHolder(clothesItems);
    }

    @Override
    public void onBindViewHolder(@NonNull ClothesViewHolder holder, int position) {
        holder.clothesTitle.setText(clothes.get(position).getTitle());
        holder.infoClothes.setText(clothes.get(position).getInfo());
    }

    @Override
    public int getItemCount() {
        return clothes.size();
    }

    public static final class ClothesViewHolder extends RecyclerView.ViewHolder{

        TextView clothesTitle, infoClothes;

        public ClothesViewHolder(@NonNull View itemView) {
            super(itemView);

            clothesTitle = itemView.findViewById(R.id.upClothes);
            infoClothes = itemView.findViewById(R.id.forUpClothes);
        }
    }

}

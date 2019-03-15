package com.openclassrooms.realestatemanager.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RealEstateAdapter extends RecyclerView.Adapter<RealEstateAdapter.ViewHolder> {

    private List<RealEstate> realEstateList;

    public RealEstateAdapter(List<RealEstate> realEstateList) {
        this.realEstateList = realEstateList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.listining_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RealEstate realEstate = realEstateList.get(position);

        holder.description.setText(realEstate.getDescription());
        holder.price.setText("$" + realEstate.getPriceInDollars());
        holder.type.setText(realEstate.getType());
        Picasso.get().load(realEstate.getPhotos().get(0)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (realEstateList == null) return 0;
        return realEstateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView type;
        private TextView description;
        private TextView price;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.listining_item_image_view);
            type = itemView.findViewById(R.id.listining_item_type);
            description = itemView.findViewById(R.id.listining_item_description);
            price = itemView.findViewById(R.id.listining_item_price);
        }
    }
}

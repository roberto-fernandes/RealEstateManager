package com.openclassrooms.realestatemanager.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.RealEstateListing;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder> {

    private List<RealEstateListing> realEstateListingList;

    public ListingAdapter(List<RealEstateListing> realEstateListingList) {
        this.realEstateListingList = realEstateListingList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.listining_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RealEstateListing realEstateListing = realEstateListingList.get(position);

        holder.description.setText(realEstateListing.getDescription());
        holder.price.setText("$" + realEstateListing.getPriceInDollars());
        holder.type.setText(realEstateListing.getType());
        Picasso.get().load(realEstateListing.getPhotos().get(0)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (realEstateListingList == null) return 0;
        return realEstateListingList.size();
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

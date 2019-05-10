package com.openclassrooms.realestatemanager.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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
    private int selectedPosition = 0;
    private Context context;
    private OnItemSelectedListener listener = null;

    public RealEstateAdapter(Context context, List<RealEstate> realEstateList) {
        this.context = context;
        this.realEstateList = realEstateList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.listining_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        RealEstate realEstate = realEstateList.get(position);

        holder.description.setText(realEstate.getDescription());
        holder.price.setText("$" + realEstate.getPrice());
        holder.type.setText(realEstate.getType());
        Picasso.get().load(realEstate.getPhotos().get(0)).into(holder.imageView);

        if (selectedPosition == position) {
            holder.cardView.setBackgroundColor(
                    context.getResources().getColor(R.color.colorCustomGrey));
        } else {
            holder.cardView.setBackgroundColor(
                    context.getResources().getColor(R.color.white));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
                listener.onSelection(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (realEstateList == null) return 0;
        return realEstateList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView type;
        private TextView description;
        private TextView price;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.listining_item_image_view);
            type = itemView.findViewById(R.id.listining_item_type);
            description = itemView.findViewById(R.id.listining_item_description);
            price = itemView.findViewById(R.id.listining_item_price);
            cardView = itemView.findViewById(R.id.listining_item_card_view);
        }
    }

    public void setOnSelectionItem(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void onSelection(int position);
    }
}

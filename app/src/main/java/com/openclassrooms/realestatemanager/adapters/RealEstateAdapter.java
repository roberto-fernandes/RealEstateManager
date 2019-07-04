package com.openclassrooms.realestatemanager.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RealEstateAdapter extends RecyclerView.Adapter<RealEstateAdapter.ViewHolder> {

    private List<RealEstate> realEstateList;
    private int selectedPosition = 0;
    private Context context;
    private OnItemSelectedListener listener = null;
    private String currency;

    public RealEstateAdapter(Context context, List<RealEstate> realEstateList, String currency) {
        this.context = context;
        this.realEstateList = realEstateList;
        this.currency = currency;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.listining_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        RealEstate realEstate = realEstateList.get(position);

        holder.description.setText(realEstate.getDescription());

        int price = -1;
        try {
            price = Integer.parseInt(realEstate.getPrice());
        } catch (Exception ignored) {
        }

        if (price != -1) {
            if (currency.equals(Constants.Currencies.DOLLAR)) {
                holder.price.setText("$" + price);
            } else if (currency.equals(Constants.Currencies.EURO)) {
                holder.price.setText("€" + Utils.convertDollarToEuro(price));
            }
        } else {
            holder.price.setText(context.getString(R.string.price_not_set_yet));
        }

        holder.type.setText(realEstate.getType());
        holder.status.setText(realEstate.getStatus());
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

    public void setCurrency(String currency) {
        this.currency = currency;
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
        private TextView status;

        ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.listining_item_image_view);
            type = itemView.findViewById(R.id.listining_item_type);
            description = itemView.findViewById(R.id.listining_item_description);
            price = itemView.findViewById(R.id.listining_item_price);
            cardView = itemView.findViewById(R.id.listining_item_card_view);
            status = itemView.findViewById(R.id.listining_item_status);
        }
    }

    public void setOnSelectionItem(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void onSelection(int position);
    }
}

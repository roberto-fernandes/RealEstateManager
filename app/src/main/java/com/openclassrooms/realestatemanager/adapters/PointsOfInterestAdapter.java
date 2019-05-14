package com.openclassrooms.realestatemanager.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;

import java.util.List;

public class PointsOfInterestAdapter extends RecyclerView.Adapter<PointsOfInterestAdapter.ViewHolder> {

    private List<String> pointsOfInterest;
    private DeleteItemListener deleteItemListener = null;

    public PointsOfInterestAdapter(List<String> pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.points_of_interest_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.textView.setText(pointsOfInterest.get(i));
    }

    @Override
    public int getItemCount() {
        if (pointsOfInterest == null) return 0;
        return pointsOfInterest.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView deleteIcon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.points_of_interest_text_view);
            deleteIcon = itemView.findViewById(R.id.points_of_interest_item_delete_icon);
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteItemListener != null) {
                        deleteItemListener.onDeleteIconPress(getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setDeleteItemListener(DeleteItemListener deleteItemListener) {
        this.deleteItemListener = deleteItemListener;
    }

    public interface DeleteItemListener {
        void onDeleteIconPress(int position);
    }
}

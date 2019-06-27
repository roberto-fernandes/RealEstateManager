package com.openclassrooms.realestatemanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;

import java.util.List;

public class VerticalListAdapter extends RecyclerView.Adapter<VerticalListAdapter.ViewHolder> {
    private List<String> stringList;

    public VerticalListAdapter(List<String> stringList) {
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.vertical_list_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String text = stringList.get(i);
        viewHolder.pointOfInterestTextView.setText(text);
    }

    @Override
    public int getItemCount() {
        if (stringList == null) {
            return 0;
        }
        return stringList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView pointOfInterestTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            pointOfInterestTextView = itemView.findViewById(R.id.points_of_interest_text_view);
        }
    }
}

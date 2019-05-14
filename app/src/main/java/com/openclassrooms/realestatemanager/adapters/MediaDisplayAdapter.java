package com.openclassrooms.realestatemanager.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MediaDisplayAdapter extends RecyclerView.Adapter<MediaDisplayAdapter.ViewHolder> {

    private List<String> mediaList;
    private boolean displayRemoveIcon;
    private Context context;
    private ItemDeleteListener itemDeleteListener = null;

    public MediaDisplayAdapter(List<String> mediaList, boolean removeIcon, Context context) {
        this.mediaList = mediaList;
        this.displayRemoveIcon = removeIcon;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.media_display_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (displayRemoveIcon) {
            viewHolder.deleteIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.deleteIcon.setVisibility(View.INVISIBLE);
        }

        if (Utils.isInternetAvailable(context)) {
            Picasso.get().load(mediaList.get(i)).into(viewHolder.imageView);
        } else {
            viewHolder.imageView.setImageDrawable(context.
                    getResources().getDrawable(R.drawable.internet_access_error));
            viewHolder.urlTextView.setText(mediaList.get(i));
        }

    }

    @Override
    public int getItemCount() {
        if (mediaList == null) {
            return 0;
        }
        return mediaList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageView deleteIcon;
        private TextView urlTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.media_display_item_image_view);
            deleteIcon = itemView.findViewById(R.id.media_display_item_delete_icon);
            urlTextView = itemView.findViewById(R.id.media_display_item_url);

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemDeleteListener != null) {
                        itemDeleteListener.deleteIconClicked(getAdapterPosition());
                    }
                }
            });
        }
    }

    public void setOnDeleteIconListener(ItemDeleteListener itemDeleteListener) {
        this.itemDeleteListener = itemDeleteListener;
    }

    public interface ItemDeleteListener {
        void deleteIconClicked(int position);
    }
}

package com.learning.skilclasses.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.learning.skilclasses.R;
import com.learning.skilclasses.models.Video;
import com.learning.skilclasses.preferences.UserSession;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.OkHttpClient;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> implements Filterable {

    private List<Video> videoList;
    private List<Video> listFull;
    private Context context;
    UserSession userSession;
    private OkHttpClient okHttpClient;

    public VideoAdapter(List<Video> messageList, Context context) {
        this.videoList = messageList;
        this.context = context;
        userSession = new UserSession(context);
        this.listFull = new ArrayList<>(videoList);
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layout, parent, false);
        return new VideoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        Video video = videoList.get(position);
        try {
            holder.title.setText(video.getVdesp());
            String sub = video.getVsubject();
            holder.videoSuject.setText("[ " + sub.substring(0, 1).toUpperCase() + sub.substring(1) + " ]");
            holder.jcVideoPlayerStandard.setUp(video.getvUrl(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "");
            Picasso.get().load(video.getThumbnailimg()).into(holder.jcVideoPlayerStandard.thumbImageView);
            //holder.jcVideoPlayerStandard.thumbImageView./*setImageDrawable(context.getDrawable(R.drawable.image_1));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        @BindView(R.id.video_title)
        TextView title;
        @BindView(R.id.video_subject)
        TextView videoSuject;
        @BindView(R.id.videoplayer)
        JCVideoPlayerStandard jcVideoPlayerStandard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            card=itemView.findViewById(R.id.cardview);
        }
    }

    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Video> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(listFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Video items : listFull) {
                    if (items.getVdesp().toLowerCase().contains(filterPattern)) {
                        filteredList.add(items);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            videoList.clear();
            videoList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

}

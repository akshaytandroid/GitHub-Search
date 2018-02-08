package com.akshay.gitrepoproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by akshaythalakoti on 2/7/18.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder> {

    private Context mContext;
    private GitHubResponseBean responseBean;
    private OnItemClickListener onItemClickListener;

    public RecyclerAdapter(Context mContext, GitHubResponseBean bean) {
        this.mContext = mContext;
        this.responseBean = bean;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repo_list, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.repoName.setText(responseBean.getItems().get(position).getName());
        holder.repoDescription.setText(String.format("Description: %s", responseBean.getItems().get(position).getDescription()));
        holder.repoNumberOfStars.setText(String.format("%s★ ",String.valueOf(responseBean.getItems().get(position).getStargazers_count())));
        holder.itemMainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClicked(responseBean, holder.getAdapterPosition());
            }
        });

        Glide.with(mContext)
                .load(responseBean.getItems().get(position).getOwner().getAvatar_url())
                .into(holder.repoAvatarImageView);        // TO Display the Image hosted Online directly to the ImageView


    }

    @Override
    public int getItemCount() {
        return responseBean.getItems().size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private ImageView repoAvatarImageView;
        private TextView repoName, repoDescription, repoNumberOfStars;
        private LinearLayout itemMainLinearLayout;

        public CustomViewHolder(View itemView) {
            super(itemView);
            repoName = itemView.findViewById(R.id.repo_name);
            repoAvatarImageView = itemView.findViewById(R.id.repo_owner_avatar);
            repoDescription = itemView.findViewById(R.id.repo_description);
            repoNumberOfStars = itemView.findViewById(R.id.repo_number_of_stars);
            itemMainLinearLayout = itemView.findViewById(R.id.item_main_linear_layout);
        }
    }

    public interface OnItemClickListener {
        void onItemClicked(GitHubResponseBean responseBean, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

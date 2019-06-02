package com.cl.testapp.dili;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cl.testapp.R;
import com.cl.testapp.dili.entity.DBangumi;
import com.cl.testapp.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 嘀哩嘀哩动画番剧
 */
public class BangumiAdapter extends RecyclerView.Adapter<BangumiAdapter.ViewHolder> {

    private List<DBangumi> mBangumiList;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View paramView, DBangumi bangumi);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public BangumiAdapter(List<DBangumi> list) {
        this.mBangumiList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.d_bangumi_recycle_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.bindItem(mBangumiList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return this.mBangumiList == null ? 0 : mBangumiList.size();
    }

    public void setData(List<DBangumi> list) {
        mBangumiList = list;
        notifyDataSetChanged();
    }

    public void updateData(List<DBangumi> list) {
        mBangumiList.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.arc_pic)
        ImageView mArcPic;
        @BindView(R.id.arc_name)
        TextView mArcName;
        private DBangumi mCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            int width = (Utils.getScreenWidth() - Utils.dp2px(32)) / 3;
            int height = width * 13 / 9;
            ViewGroup.LayoutParams localLayoutParams = mArcPic.getLayoutParams();
            localLayoutParams.width = width;
            localLayoutParams.height = height;
            mArcPic.setLayoutParams(localLayoutParams);

            ViewGroup.LayoutParams layoutParams = mArcName.getLayoutParams();
            layoutParams.width = width;
            mArcName.setLayoutParams(layoutParams);

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, mCategory);
                    }
                }
            });

        }

        public void bindItem(DBangumi bangumi, int position) {
            mCategory = bangumi;
            mArcName.setText(bangumi.getTypename());

            Glide.with(mArcPic.getContext())
                    .load(bangumi.getSuoluetudizhi())
                    .centerCrop()
                    .placeholder(R.color.c)
                    .into(mArcPic);
        }
    }

}

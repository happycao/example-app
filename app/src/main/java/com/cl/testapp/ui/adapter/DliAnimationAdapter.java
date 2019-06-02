package com.cl.testapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cl.testapp.R;
import com.cl.testapp.model.Animation;
import com.cl.testapp.model.DliAnimation;
import com.cl.testapp.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 嘀哩嘀哩动画番剧
 */
public class DliAnimationAdapter extends RecyclerView.Adapter<DliAnimationAdapter.ViewHolder> {

    private List<Animation> mAnimationList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View paramView, Animation animation);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public DliAnimationAdapter(Context context, List<DliAnimation> animationList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
        for (int i = 0; i < animationList.size(); i++) {
            this.mAnimationList.addAll(animationList.get(i).getAnimationList());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(this.mInflater.inflate(R.layout.dili_recycle_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bindView(mContext, mAnimationList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.mAnimationList == null ? 0 : mAnimationList.size();
    }

    public void setData(List<DliAnimation> animationList){
        mAnimationList.clear();
        for (DliAnimation animation: animationList){
            mAnimationList.addAll(animation.getAnimationList());
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.animate_img)
        ImageView mAnimateImg;
        @BindView(R.id.animate_name)
        TextView mAnimateName;
        private Animation mAnimation;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(Context context, Animation animation) {
            mAnimation = animation;
            mAnimateName.setText(animation.getName());
            int i = (Utils.getScreenWidth() - Utils.dp2px(16)) / 3;
            int j = i * 13 / 9;
            ViewGroup.LayoutParams localLayoutParams = mAnimateImg.getLayoutParams();
            localLayoutParams.width = i;
            localLayoutParams.height = j;
            mAnimateImg.setLayoutParams(localLayoutParams);
            Glide.with(mContext).load(animation.getImgUrl()).into(mAnimateImg);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, mAnimation);
                    }
                }
            });
        }
    }

}

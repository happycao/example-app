package com.cl.testapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cl.testapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ImageAdapter
 * Created by Administrator on 2017-02-07.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> mImgUrl;
    private boolean[] mSelect;
    private int savePosition = 0;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public ImageAdapter(Context context, List<String> imgUrl) {
        this.mContext = context;
        this.mImgUrl = imgUrl;
        this.mSelect = new boolean[mImgUrl.size()];
        mSelect[0] = true;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_img, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Glide.with(holder.mItemImg.getContext())
                .load(mImgUrl.get(position))
                .into(holder.mItemImg);
        holder.mItemCard.setSelected(mSelect[position]);
        holder.mItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelect[savePosition] = false;
                savePosition = position;
                mSelect[position] = true;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImgUrl == null? 0: mImgUrl.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_img)
        ImageView mItemImg;
        @BindView(R.id.item_card)
        RelativeLayout mItemCard;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

package com.cl.testapp.widget.areaselect;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cl.testapp.R;

import java.util.List;

/**
 * 地区选择
 * Created by cl on 2016/9/27.
 */
public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mData;
    private int mSelectPosition = -1;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public AreaAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.mData = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_area, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mData != null) {
            holder.position = position;
            holder.mTextView.setText(mData.get(position));
            if (mSelectPosition == position) {
                holder.mTextView.setTextColor(Color.parseColor("#ffff4444"));
                holder.mImageView.setVisibility(View.VISIBLE);
            } else {
                holder.mTextView.setTextColor(Color.parseColor("#ff757575"));
                holder.mImageView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    /**
     * 设置选中
     * @param position 位置
     */
    public void setSelectPosition(int position) {
        mSelectPosition = position;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;
        private ImageView mImageView;
        private int position;

        public ViewHolder(final View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_area);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_select);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) mOnItemClickListener.onItemClick(v, position);
        }
    }
}

package com.cl.testapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cl.testapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * CheckboxAdapter
 * Created by Administrator on 2016-08-22.
 */
public class CheckboxAdapter extends RecyclerView.Adapter<CheckboxAdapter.MyViewHolder> {

    private List<String> mDatas;
    private boolean[] mCheckbox;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public CheckboxAdapter(Context context, List<String> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
        this.mCheckbox = new boolean[mDatas.size()];
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_check_box, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.mTvName.setText(mDatas.get(position));
        holder.mCheckbox.setChecked(mCheckbox[position]);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                if (holder.mCheckbox.isChecked()) {
                    holder.mCheckbox.setChecked(false);
                    mCheckbox[position] = false;
                } else {
                    holder.mCheckbox.setChecked(true);
                    mCheckbox[position] = true;
                }
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setCheckAll(boolean[] checkbox) {
        mCheckbox = checkbox;
        notifyDataSetChanged();
    }


    public boolean[] getCheckAll() {
        return mCheckbox;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checkbox)
        CheckBox mCheckbox;
        @BindView(R.id.tv_name)
        TextView mTvName;
        View card;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            card = view;

        }
    }

}

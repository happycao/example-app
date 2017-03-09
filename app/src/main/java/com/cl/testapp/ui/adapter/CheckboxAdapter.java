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
    private boolean[] mChecks;
    private Context mContext;

    private boolean mIsEdit;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onEditItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public CheckboxAdapter(Context context, List<String> mDatas) {
        this.mContext = context;
        this.mDatas = mDatas;
        this.mChecks = new boolean[mDatas.size()];
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_check_box, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.bindItem(mDatas.get(position), mIsEdit);


    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setEdit(boolean isEdit){
        this.mIsEdit = isEdit;
        notifyDataSetChanged();
    }

    public boolean getEditStatus(){
        return mIsEdit;
    }

    public void setCheckAll(boolean[] checkbox) {
        mChecks = checkbox;
        notifyDataSetChanged();
    }


    public boolean[] getCheckAll() {
        return mChecks;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.checkbox)
        CheckBox mCheckbox;
        @BindView(R.id.tv_name)
        TextView mTvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindItem(String str, boolean isEdit){
            final int position = getLayoutPosition();
            mTvName.setText(str);
            if (isEdit) {
                mCheckbox.setVisibility(View.VISIBLE);
                mCheckbox.setChecked(mChecks[position]);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCheckbox.isChecked()) {
                            mCheckbox.setChecked(false);
                            mChecks[position] = false;
                        } else {
                            mCheckbox.setChecked(true);
                            mChecks[position] = true;
                        }
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onEditItemClick(itemView, position);
                        }
                    }
                });
            } else {
                mCheckbox.setVisibility(View.GONE);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(itemView, position);
                        }
                    }
                });
            }
        }
    }

}

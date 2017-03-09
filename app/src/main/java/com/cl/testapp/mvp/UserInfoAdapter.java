package com.cl.testapp.mvp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cl.testapp.R;
import com.cl.testapp.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * UserInfoAdapter
 * Created by Administrator on 2016-08-22.
 */
public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder> {

    private List<User> mUserList;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public UserInfoAdapter(Context context, List<User> userList) {
        this.mContext = context;
        setUserList(userList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_user_task, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mPosition = position;
        User user = mUserList.get(position);
        holder.mTvName.setText(user.getName());
        holder.mTvTask.setText(user.getInfo());
        holder.mCheckbox.setChecked(user.getIsDel());
    }

    @Override
    public int getItemCount() {
        return mUserList == null ? 0 : mUserList.size();
    }

    private void setUserList(List<User> userList) {
        mUserList = userList;
    }

    public void replaceData(List<User> userList) {
        setUserList(userList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.checkbox)
        CheckBox mCheckbox;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_task)
        TextView mTvTask;
        public int mPosition;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mCheckbox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(itemView, mPosition);
            }
        }
    }

}

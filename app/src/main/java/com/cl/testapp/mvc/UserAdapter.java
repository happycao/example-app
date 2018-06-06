package com.cl.testapp.mvc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cl.testapp.R;
import com.cl.testapp.util.ColorPhrase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * UserAdapter
 * Created by Administrator on 2016-08-22.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private static final String TAG = "xl";

    private List<UserInfo> mUserList;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public UserAdapter(Context context, List<UserInfo> userList) {
        this.mContext = context;
        this.mUserList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        UserInfo user = mUserList.get(position);
        holder.mUserName.setText(user.getUsername());
        if (position % 2 == 0) {
            Glide.with(holder.mUserImg.getContext())
                    .load("https://a-ssl.duitang.com/uploads/item/201701/10/20170110214207_Ecjy5.thumb.700_0.jpeg")
                    .bitmapTransform(new CropCircleTransformation(holder.mUserImg.getContext()))
                    .into(holder.mUserImg);
            String pattern = "左手{代码}，右手{诗歌}";
            CharSequence chars = ColorPhrase.from(pattern).withSeparator("{}").innerColor(0xFFE6454A).outerColor(0xFF666666).format();
            holder.mUserInfo.setText(chars);
            holder.mUserSex.setImageResource(R.mipmap.ic_man);
        } else {
            Glide.with(holder.mUserImg.getContext())
                    .load("https://a-ssl.duitang.com/uploads/item/201611/24/20161124205521_KTLzw.thumb.700_0.jpeg")
                    .bitmapTransform(new CropCircleTransformation(holder.mUserImg.getContext()))
                    .into(holder.mUserImg);
            holder.mUserInfo.setText("人生还有诗和远方");
            holder.mUserSex.setImageResource(R.mipmap.ic_woman);
        }
    }

    @Override
    public int getItemCount() {
        return mUserList == null ? 0 : mUserList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_img)
        ImageView mUserImg;
        @BindView(R.id.user_name)
        TextView mUserName;
        @BindView(R.id.user_sex)
        ImageView mUserSex;
        @BindView(R.id.user_info)
        TextView mUserInfo;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}

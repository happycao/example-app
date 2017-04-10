package com.cl.testapp.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cl.testapp.R;
import com.cl.testapp.model.GoBean;
import com.cl.testapp.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.OkHttpClient;

import static com.cl.testapp.util.Util.getScreenWidth;

/**
 * 主页Adapter
 * Created by Administrator on 2016-08-22.
 */
public class WaterfallAdapter extends RecyclerView.Adapter<WaterfallAdapter.MyViewHolder> {

    private List<GoBean> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private OkHttpClient client = new OkHttpClient();

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public WaterfallAdapter(Context context, List<GoBean> data) {
        this.mContext = context;
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.item_home, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.mPosition = position;
        //用Glide加载返回
        Glide.with(mContext)
                .load(mData.get(position).getImgUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        //拿到Bitmap，设置ImageView宽高
                        int screenWidth = getScreenWidth(mContext);
                        int width = (screenWidth - Util.dip2px(mContext, 32)) / 3;
                        int height = width * resource.getHeight() / resource.getWidth();
                        ViewGroup.LayoutParams lp = holder.mImgItem.getLayoutParams();
                        lp.width = width;
                        lp.height = height;
                        holder.mImgItem.setLayoutParams(lp);
                        Glide.with(mContext)
                                .load(mData.get(position).getImgUrl())
                                .placeholder(R.color.c)
                                .bitmapTransform(new RoundedCornersTransformation(mContext, Util.dip2px(mContext, 5), 0, RoundedCornersTransformation.CornerType.ALL))
                                .crossFade()
                                .into(holder.mImgItem);
                    }
                });

        holder.mTvName.setText(mData.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addData(int position, GoBean data) {
        mData.add(position, data);
        notifyItemInserted(position);
    }


    public void removeData(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_item)
        ImageView mImgItem;
        @BindView(R.id.tv_name)
        TextView mTvName;
        private int mPosition;

        public MyViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // 如果设置了回调，则设置点击事件
            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(v, mPosition);
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnItemClickListener.onItemLongClick(itemView, mPosition);
                        return false;
                    }
                });
            }
        }

    }

}

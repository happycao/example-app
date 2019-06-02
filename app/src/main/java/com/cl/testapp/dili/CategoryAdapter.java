package com.cl.testapp.dili;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cl.testapp.R;
import com.cl.testapp.dili.entity.DCategory;
import com.cl.testapp.util.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * author : Bafs
 * e-mail : bafs.jy@live.com
 * time   : 2017/09/07
 * desc   :
 * version: 1.0
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<DCategory> mList;

    private OnItemListener mOnItemListener;

    public interface OnItemListener {
        void onItemClick(View view, DCategory category);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.mOnItemListener = onItemListener;
    }

    CategoryAdapter(List<DCategory> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.d_category_recycle_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItem(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setData(List<DCategory> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void updateData(List<DCategory> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.arc_type_image)
        ImageView mArcTypeImage;
        @BindView(R.id.arc_type_name)
        TextView mArcTypeName;

        private DCategory mCategory;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // 设置图片宽高
            int screenWidth = Utils.getScreenWidth();
            int px = Utils.dp2px(10);
            int itemSize = (screenWidth - (px * 12)) / 3;
            ViewGroup.LayoutParams layoutParams = mArcTypeImage.getLayoutParams();
            layoutParams.width = itemSize;
            layoutParams.height = itemSize;
            mArcTypeImage.setLayoutParams(layoutParams);

            // 绑定事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemListener != null) mOnItemListener.onItemClick(v, mCategory);
                }
            });
        }

        /**
         * 数据绑定
         */
        void bindItem(DCategory category) {
            mCategory = category;
            mArcTypeName.setText(category.getTypename());

            Glide.with(mArcTypeImage.getContext())
                    .load(category.getSuoluetudizhi())
                    .centerCrop()
                    .placeholder(R.color.c)
                    .bitmapTransform(new CropCircleTransformation(mArcTypeImage.getContext()))
                    .crossFade()
                    .into(mArcTypeImage);
        }
    }
}

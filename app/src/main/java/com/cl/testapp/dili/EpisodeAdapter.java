package com.cl.testapp.dili;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cl.testapp.R;
import com.cl.testapp.dili.entity.DEpisode;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author : Bafs
 * e-mail : bafs.jy@live.com
 * time   : 2019/05/07
 * desc   : 剧集
 * version: 1.0
 */
public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.ViewHolder> {

    private List<DEpisode> mList;
    private boolean[] mSelect;
    private int mSavePosition;

    private OnItemListener mOnItemListener;

    public interface OnItemListener {
        void onItemClick(View view, DEpisode episode, int position);
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.mOnItemListener = onItemListener;
    }

    public EpisodeAdapter(List<DEpisode> list) {
        this.mList = list;
        int size = list.size();
        this.mSelect = new boolean[size];
        if (size != 0) {
            this.mSavePosition = size - 1;
            this.mSelect[mSavePosition] = true;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.d_episode_recycle_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItem(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setData(List<DEpisode> list) {
        this.mList = list;
        int size = list.size();
        this.mSelect = new boolean[size];
        if (size != 0) {
            this.mSavePosition = size - 1;
            this.mSelect[mSavePosition] = true;
        }
        notifyDataSetChanged();
    }

    public void updateData(List<DEpisode> list) {
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.arc_hive_name)
        TextView mArcTypeName;

        private String regex = "^[-\\+]?[\\d]*$";
        private DEpisode mEpisode;
        private int mPosition;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemListener != null) {
                        mSelect[mSavePosition] = false;
                        mSavePosition = mPosition;
                        mSelect[mPosition] = true;
                        notifyDataSetChanged();
                        mOnItemListener.onItemClick(v, mEpisode, mPosition);
                    }
                }
            });
        }

        public void bindItem(DEpisode episode, int position) {
            mPosition = position;
            mEpisode = episode;
            Pattern pattern = Pattern.compile(regex);
            String writer = episode.getWriter();
            if (pattern.matcher(writer).matches()) {
                writer = String.format("第 %s 话", writer);
            }
            mArcTypeName.setText(writer);
            mArcTypeName.setSelected(mSelect[mPosition]);
        }
    }
}

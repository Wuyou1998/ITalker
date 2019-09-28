package com.wy.italker.fragments.panel;

import android.view.View;

import com.wy.common.face.Face;
import com.wy.common.widget.recycler.RecyclerAdapter;
import com.wy.italker.R;

import java.util.List;


public class FaceAdapter extends RecyclerAdapter<Face.Bean> {

    public FaceAdapter(List<Face.Bean> beans, AdapterListener<Face.Bean> listener) {
        super(beans, listener);
    }

    @Override
    protected int getItemViewType(int position, Face.Bean bean) {
        return R.layout.cell_face;
    }

    @Override
    protected ViewHolder<Face.Bean> onCreateViewHolder(View root, int viewType) {
        return new FaceHolder(root);
    }
}

package newer.com.schoolgo.ui.adpater.baseadpater;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * Created by Administrator on 2017/3/3.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    private View mConvertView;
    private SparseArray<View> mViews;

    private ViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    /**
     * 创建ViewHolder
     *
     * @param context
     * @param resLayout
     * @param root
     * @return ViewHolder
     */
    static ViewHolder creatByLayout(Context context, int resLayout, ViewGroup root) {
        View view = LayoutInflater.from(context).inflate(resLayout, root, false);
        return new ViewHolder(view);
    }

    static ViewHolder create(View view) {
        return new ViewHolder(view);
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过id获得控件
     */
    private <T extends View> T getViewById(int resId) {
        View view = mViews.get(resId);
        if (view == null) {
            view = mConvertView.findViewById(resId);
            mViews.put(resId, view); //通过SparseArray将子View放入
        }
        return (T) view;
    }

    /**
     * 设置TextView文本信息
     */
    public ViewHolder setText(int resId, String content) {
        TextView textView = getViewById(resId);
        textView.setText(content);
        return this;
    }

    /**
     * 设置九宫格图片
     *
     * @param resId
     * @param img
     * @return
     */
    public ViewHolder setImgNine(int resId, ArrayList<String> img) {
        BGANinePhotoLayout ninePhotoLayout = getViewById(resId);
        ninePhotoLayout.setData(img);
        return this;
    }

    /**
     * 设置head图像
     *
     * @param resId
     * @param img
     * @return
     */
    public ViewHolder setImg(int resId, Bitmap img) {
        ImageView mImg = getViewById(resId);
        mImg.setImageBitmap(img);
        return this;
    }
}

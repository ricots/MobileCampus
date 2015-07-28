package com.unique.daiyiming.ilovecollege.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.unique.daiyiming.ilovecollege.Config.Config;
import com.unique.daiyiming.ilovecollege.ScreenParams.ScreenParams;
import com.unique.daiyiming.ilovecollege.R;
import com.unique.daiyiming.ilovecollege.View.ScaleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TopicListAdapter extends BaseAdapter {

    //键值
    public final static String MAP_KEY_ID = "id";
    public final static String MAP_KEY_GENDER = "gender";
    public final static String MAP_KEY_CONTENT = "content";
    public final static String MAP_KEY_VOICE = "voice";
    public final static String MAP_KEY_IMAGE_COUNT = "image_count";
    public final static String MAP_KEY_IMAGE = "first_image";
    public final static String MAP_KEY_LIKE_COUNT = "like_count";
    public final static String MAP_KEY_REPLY_COUNT = "reply_count";
    public final static String MAP_KEY_IS_LIKED = "is_liked";

    //性别
    private final static int GENDER_MAN = 0;
    private final static int GENDER_WOMAN = 1;

    //是否点赞
    public final static int IS_LIKE_TRUE = 1;
    public final static int IS_LIKE_FALSE = 0;

    private List<Map<String, String>> itemList = null;
    private Context context = null;
    private int itemHeight = 0;
    private int itemWidth = 0;
    private float scale = 0;
    private DisplayImageOptions options = null; //ImageLoader选项

    private OnTopicListControlButtonClickListener controlButtonClickListener = null; //点赞评论按钮点击监听

    /**
     * 评论点赞按钮点击监听
     */
    public interface OnTopicListControlButtonClickListener {
        void OnLikeButtonClickedListener(int position, int isLiked, String topicId);
        void OnCommentButtonClickedListener(int positon);
    }

    //组件控制器
    private class ViewHolder {
        public RelativeLayout rl_mainView = null; //主背景
        public ImageView img_first = null; //第一张背景预览图
        public RelativeLayout rl_imageContainer = null; //图片容器
        public LinearLayout ll_imageCountContainer = null; //图片数目容器
        public TextView tv_imageCount = null; //图片数目
        public TextView tv_content = null; //主要文字内容
        public ImageView img_voice = null; //语音图片
        public LinearLayout ll_controlBar = null; //控制bar
        public TextView tv_contentPrev = null; //主要内容预览
        public ScaleImageView simg_like = null; //喜欢图标
        public TextView tv_likeConnt = null; //喜欢数目
        public ScaleImageView simg_comment = null; //评论图标
        public TextView tv_replayCount = null; //回复数目
    }

    public TopicListAdapter(Context context) {
        this.context = context;
        itemList = new ArrayList<>();
        itemHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, context.getResources().getDisplayMetrics());
        itemWidth = (new ScreenParams(context)).getScreenWidth();
        scale = (float) itemWidth / itemHeight;
        options = new DisplayImageOptions.Builder().cacheInMemory(true).build();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.listview_topics_item_layout, null);
            holder.rl_mainView = (RelativeLayout) convertView.findViewById(R.id.rl_mainView);
            holder.img_first = (ImageView) convertView.findViewById(R.id.img_first);
            holder.rl_imageContainer = (RelativeLayout) convertView.findViewById(R.id.rl_imageContainer);
            holder.ll_imageCountContainer = (LinearLayout) convertView.findViewById(R.id.ll_imageCountContainer);
            holder.tv_imageCount = (TextView) convertView.findViewById(R.id.tv_imageCount);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.img_voice = (ImageView) convertView.findViewById(R.id.img_voice);
            holder.ll_controlBar = (LinearLayout) convertView.findViewById(R.id.ll_controlBar);
            holder.tv_contentPrev = (TextView) convertView.findViewById(R.id.tv_contentPrev);
            holder.simg_like = (ScaleImageView) convertView.findViewById(R.id.simg_like);
            holder.tv_likeConnt = (TextView) convertView.findViewById(R.id.tv_likeConnt);
            holder.simg_comment = (ScaleImageView) convertView.findViewById(R.id.simg_comment);
            holder.tv_replayCount = (TextView) convertView.findViewById(R.id.tv_replayCount);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //设置高度
        AbsListView.LayoutParams mainLayoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, itemHeight);
        convertView.setLayoutParams(mainLayoutParams);

        //绑定数据
        final Map<String, String> item = itemList.get(position);
        //初始化背景
        if (Integer.parseInt(item.get(MAP_KEY_GENDER)) == GENDER_MAN) { //男生
            holder.rl_mainView.setBackgroundColor(Color.parseColor("#3079A6"));
            holder.ll_controlBar.setBackgroundColor(Color.parseColor("#246792"));
        } else if (Integer.parseInt(item.get(MAP_KEY_GENDER)) == GENDER_WOMAN) { //女生
            holder.rl_mainView.setBackgroundColor(Color.parseColor("#DD7D7E"));
            holder.ll_controlBar.setBackgroundColor(Color.parseColor("#D06E6F"));
        }

        if (!item.get(MAP_KEY_VOICE).equals("")) { //语音+图片
            holder.img_voice.setVisibility(View.VISIBLE);
            holder.tv_content.setVisibility(View.GONE);
            holder.tv_contentPrev.setVisibility(View.INVISIBLE);
        } else { //文字+图片
            holder.img_voice.setVisibility(View.GONE);
            holder.tv_content.setVisibility(View.VISIBLE);
            holder.tv_contentPrev.setVisibility(View.INVISIBLE);

            holder.tv_content.setText(item.get(MAP_KEY_CONTENT));
            holder.tv_contentPrev.setText(item.get(MAP_KEY_CONTENT));
        }

        holder.img_first.setImageBitmap(null); //防止图片串联

        //点赞
        if (Integer.parseInt(itemList.get(position).get(MAP_KEY_IS_LIKED)) == IS_LIKE_TRUE) { //已经点赞
            holder.simg_like.setImageResource(R.drawable.icon_like);
        } else if (Integer.parseInt(itemList.get(position).get(MAP_KEY_IS_LIKED)) == IS_LIKE_FALSE) { //还没有点赞
            holder.simg_like.setImageResource(R.drawable.icon_dislike);
        }

        if (!item.get(MAP_KEY_IMAGE_COUNT).equals("0")) { //如果图片数不为0
            holder.ll_imageCountContainer.setVisibility(View.VISIBLE);
            holder.tv_imageCount.setText(item.get(MAP_KEY_IMAGE_COUNT));
            holder.img_first.setTag(holder);

            ImageLoader.getInstance().displayImage(Config.BASE_URL + item.get(MAP_KEY_IMAGE), holder.img_first, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    ViewHolder holder = (ViewHolder) view.getTag();
                    holder.img_first.setImageBitmap(resertBitmapShape(bitmap)); //重置Bitmap形状
                    holder.ll_controlBar.setBackgroundResource(R.drawable.topic_item_image_bar);
                    holder.tv_content.setVisibility(View.GONE);
                    holder.tv_contentPrev.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                }
            });

        } else { //如果图片数为0
            holder.ll_imageCountContainer.setVisibility(View.GONE); //隐藏图片数显示器
        }
        //初始化点赞数和喜欢数
        holder.tv_likeConnt.setText(item.get(MAP_KEY_LIKE_COUNT)); //喜欢数
        holder.tv_replayCount.setText(item.get(MAP_KEY_REPLY_COUNT)); //回复数

        //绑定事件
        holder.simg_like.setOnScaleImageViewClickListener(new ScaleImageView.OnScaleImageViewClickListener() {
            @Override
            public void OnScaleImageViewClick(View view) {
                if (controlButtonClickListener != null) {
                    controlButtonClickListener.OnLikeButtonClickedListener(position, Integer.parseInt(itemList.get(position).get(MAP_KEY_IS_LIKED)), itemList.get(position).get(MAP_KEY_ID));
                }
            }
        });

        holder.simg_comment.setOnScaleImageViewClickListener(new ScaleImageView.OnScaleImageViewClickListener() {
            @Override
            public void OnScaleImageViewClick(View view) {
            }
        });

        return convertView;
    }

    private Bitmap resertBitmapShape(Bitmap bitmap) {
        int width = 0, height = 0, x = 0, y = 0;

        if (bitmap.getHeight() * scale > bitmap.getWidth()) { //相对于比例，高比宽长
            height = (int) (bitmap.getWidth() / scale);
            width = bitmap.getWidth();
            x = 0;
            y = (bitmap.getHeight() - height) / 2;
        } else { //相对于比例， 高比宽短
            height = bitmap.getHeight();
            width = (int) (bitmap.getHeight() * scale);
            x = (bitmap.getWidth() - width) / 2;
            y = 0;
        }

        Bitmap newBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
        return newBitmap;
    }

    public void setOnTopicListControlButtonClickListener(OnTopicListControlButtonClickListener listener) {
        this.controlButtonClickListener = listener;
    }

    public void addItem(Map<String, String> item) {
        itemList.add(item);
    }

    public void clearAllItem() {
        itemList.clear();
    }

    /**
     * 给指定位置的Topic点赞
     */
    public void setItemLiked(int position) {
        Map<String, String> item = itemList.get(position);
        item.put(MAP_KEY_IS_LIKED, String.valueOf(IS_LIKE_TRUE));
        item.put(MAP_KEY_LIKE_COUNT, String.valueOf(Integer.parseInt(item.get(MAP_KEY_LIKE_COUNT)) + 1));
        this.notifyDataSetChanged();
    }

    /**
     * 取消指定位置的点赞
     */
    public void setItemUnLiked(int position) {
        Map<String, String> item = itemList.get(position);
        item.put(MAP_KEY_IS_LIKED, String.valueOf(IS_LIKE_FALSE));
        item.put(MAP_KEY_LIKE_COUNT, String.valueOf(Integer.parseInt(item.get(MAP_KEY_LIKE_COUNT)) - 1));
        this.notifyDataSetChanged();
    }
}




















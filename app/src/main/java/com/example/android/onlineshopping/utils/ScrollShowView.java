package com.example.android.onlineshopping.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.android.onlineshopping.R;

import java.util.ArrayList;
import java.util.List;

public class ScrollShowView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private List<String> imageUrlList;
    private List<String> contentList;
    private ViewPager scrollViewPager;
    private TextView scrollContent;
    private LinearLayout dotLayout;
    private List<ImageView> imageViewList;
    private ImageLoader imageLoader;
    private OnItemClickListener listener;
    private int lastPosition;
    private boolean isTouch = false;


    public ScrollShowView(@NonNull Context context) {
        super( context );
    }

    public ScrollShowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super( context, attrs );
    }

    public ScrollShowView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
    }

    private void initView(){
        if(getChildCount() == 0){
            View.inflate(getContext(), R.layout.viewpager_dot_main_category_head, this);
            scrollViewPager = findViewById(R.id.vp_scroll_view);
            scrollContent = findViewById(R.id.tv_scroll_content);
            dotLayout = findViewById(R.id.ll_dots);
        }
    }

    private void initUI(){
        imageViewList = new ArrayList<>();
        View dotView;

        //initialize the imageView and fill the view with images
        int scrollViewSize = imageUrlList.size();
        for(int i = 0; i < scrollViewSize; i++){
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            if(imageLoader != null){
                imageLoader.loadImage(imageView, imageUrlList.get(i));
            }
            imageViewList.add(imageView);

            //initialize the dot layout
            dotView = new View(getContext());

            dotView.setBackgroundResource(R.drawable.selector_dot);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;

            if(i != 0){
                dotView.setEnabled(false);
            }

            if(dotLayout.getChildCount() > imageViewList.size()){
                dotLayout.removeAllViews();
            }

            dotLayout.addView(dotView);

            dotLayout.addView(dotView, params);
        }

        ImageAdapter imageAdapter = new ImageAdapter();
        scrollViewPager.setAdapter(imageAdapter);

        scrollViewPager.addOnPageChangeListener(this);

        scrollViewPager.setCurrentItem(0);

        scrollContent.setText(contentList.get(0));
        dotLayout.getChildAt(0).setEnabled(true);
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        dotLayout.getChildAt(position).setEnabled(true);
        dotLayout.getChildAt(lastPosition).setEnabled(false);

        scrollContent.setText(contentList.get(position));
        lastPosition = position;
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    private class ImageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            if(position > imageViewList.size() - 1)
                return null;
            ImageView imageView = imageViewList.get(position);
            imageView.setOnClickListener( new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onItemClick(position, contentList.get(position));
                    }
                }
            } );

            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }


    public interface ImageLoader {
        void loadImage(ImageView imageView, String url);
    }

    public interface OnItemClickListener {

        void onItemClick(int position, String title);
    }



}

package com.david.photoalbum.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.bumptech.glide.Glide;
import com.david.photoalbum.R;
import com.david.photoalbum.databinding.ItemPhotoAlbumBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by david on 2017/4/16.
 */

public class PhotoAlbum extends RelativeLayout {

  public static final String TAG = "PhotoAlbum";

  private ViewPager viewPager;
  private Context context;
  private int albumWidth;
  private int albumHeight;
  private int albumMargin;
  private float enlargeScale;
  private List<File> albumList = new ArrayList<>();
  private List<Integer> albumResList = new ArrayList<>();
  private int photoType = 0;//0:文件;1:资源id
  private PhotoAlbum.OnPageChangedListener onPageChangedListener;
  private Map<Integer, View> albumViews = new HashMap<>();

  private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
    @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      Log.d(TAG, "" + position + ";" + positionOffset);
      View currentView = albumViews.get(position);
      ItemPhotoAlbumBinding binding = (ItemPhotoAlbumBinding) currentView.getTag();
      binding.rlContent.setScaleX(1 / (enlargeScale - (enlargeScale - 1) * (1 - positionOffset)));
      binding.rlContent.setScaleY(1 / (enlargeScale - (enlargeScale - 1) * (1 - positionOffset)));
      int nextPosition = position + 1;
      if (nextPosition < pagerAdapter.getCount()) {
        View nextView = albumViews.get(nextPosition);
        ItemPhotoAlbumBinding nextBinding = (ItemPhotoAlbumBinding) nextView.getTag();
        nextBinding.rlContent.setScaleX(1 / (enlargeScale - (enlargeScale - 1) * positionOffset));
        nextBinding.rlContent.setScaleY(1 / (enlargeScale - (enlargeScale - 1) * positionOffset));
      }

      if (onPageChangedListener != null) {
        onPageChangedListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
      }
    }

    @Override public void onPageSelected(int position) {
      if (onPageChangedListener != null) {
        onPageChangedListener.onPageChanged(position);
      }
    }

    @Override public void onPageScrollStateChanged(int state) {

    }
  };

  private PagerAdapter pagerAdapter = new PagerAdapter() {
    @Override public int getCount() {
      if (photoType == 0) {
        return albumList.size();
      } else {
        return albumResList.size();
      }
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }

    @Override public Object instantiateItem(ViewGroup container, int position) {
      View itemView = albumViews.get(position);
      if (itemView == null) {
        ItemPhotoAlbumBinding binding = ItemPhotoAlbumBinding.inflate(LayoutInflater.from(context));
        binding.ivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (photoType == 0) {
          Glide.with(context).load(albumList.get(position)).into(binding.ivPhoto);
        } else {
          Glide.with(context).load(albumResList.get(position)).into(binding.ivPhoto);
        }
        itemView = binding.getRoot();
        albumViews.put(position, itemView);
        itemView.setTag(binding);
      }
      ItemPhotoAlbumBinding binding = (ItemPhotoAlbumBinding) itemView.getTag();
      binding.rlContent.setScaleY(1 / enlargeScale);
      binding.rlContent.setScaleX(1 / enlargeScale);
      container.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
      return itemView;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView(albumViews.get(position));
    }
  };

  public PhotoAlbum(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PhotoAlbum);
    albumWidth = typedArray.getDimensionPixelSize(R.styleable.PhotoAlbum_album_width, 200);
    albumHeight = typedArray.getDimensionPixelSize(R.styleable.PhotoAlbum_album_height, 300);
    albumMargin = typedArray.getDimensionPixelSize(R.styleable.PhotoAlbum_album_margin, 10);
    enlargeScale = typedArray.getFloat(R.styleable.PhotoAlbum_enlarge_scale, 1.5f);

    initPagerView();
  }

  public void setAlbums(List<File> list) {
    albumList.clear();
    albumList.addAll(list);
    photoType = 0;
    pagerAdapter.notifyDataSetChanged();
  }

  public void setResAlbums(List<Integer> list) {
    albumResList.clear();
    albumResList.addAll(list);
    photoType = 1;
    pagerAdapter.notifyDataSetChanged();
  }

  private void initPagerView() {
    viewPager = new ViewPager(context);
    addView(viewPager, albumWidth, albumHeight);
    LayoutParams params = (LayoutParams) viewPager.getLayoutParams();
    params.addRule(CENTER_IN_PARENT);
    viewPager.setLayoutParams(params);
    viewPager.setPageMargin(albumMargin);
    viewPager.setClipChildren(false);
    viewPager.setOffscreenPageLimit(3);
    viewPager.addOnPageChangeListener(pageChangeListener);
    viewPager.setOverScrollMode(OVER_SCROLL_NEVER);

    this.setClipChildren(false);

    this.setOnTouchListener(new OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return viewPager.onTouchEvent(event);
      }
    });

    viewPager.setAdapter(pagerAdapter);
  }

  public void setOnPageChangedListener(OnPageChangedListener listener) {
    this.onPageChangedListener = listener;
  }

  public interface OnPageChangedListener {
    void onPageChanged(int position);

    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
  }
}

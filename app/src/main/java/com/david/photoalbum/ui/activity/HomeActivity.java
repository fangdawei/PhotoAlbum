package com.david.photoalbum.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.david.photoalbum.R;
import com.david.photoalbum.databinding.ActivityHomeBinding;
import com.david.photoalbum.databinding.ToolbarHomeBinding;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseBindingActivity<ActivityHomeBinding> {

  public static void startActivity(Context context) {
    Intent intent = new Intent(context, HomeActivity.class);
    context.startActivity(intent);
  }

  @Override protected View createToolBar(Bundle savedInstanceState, ViewGroup container) {
    ToolbarHomeBinding binding = ToolbarHomeBinding.inflate(getLayoutInflater(), container, false);
    binding.title.setText("相册");
    return binding.getRoot();
  }

  @Override protected ActivityHomeBinding createViewDataBinding(Bundle savedInstanceState, ViewGroup container) {
    return ActivityHomeBinding.inflate(this.getLayoutInflater(), container, false);
  }

  @Override protected void preInit(Bundle savedInstanceState) {
    super.preInit(savedInstanceState);
  }

  @Override public void initView() {

  }

  @Override public void initListener() {

  }

  @Override public void initData() {
    List<Integer> albums = new ArrayList<>();
    for(int i = 0; i< 10; i++){
      albums.add(R.drawable.photo);
    }
    mVDB.paAlbums.setResAlbums(albums);
  }
}

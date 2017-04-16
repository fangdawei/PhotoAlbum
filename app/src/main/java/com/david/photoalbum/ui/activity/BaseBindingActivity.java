package com.david.photoalbum.ui.activity;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by david on 2017/3/28.
 */

public abstract class BaseBindingActivity<T extends ViewDataBinding> extends BaseActivity {

  protected T mVDB;

  protected abstract T createViewDataBinding(Bundle savedInstanceState, ViewGroup container);

  @Override protected void preInit(Bundle savedInstanceState) {

  }

  @Override protected View createToolBar(Bundle savedInstanceState, ViewGroup container) {
    return null;
  }

  @Override protected View createContentView(Bundle savedInstanceState, ViewGroup container) {
    mVDB = createViewDataBinding(savedInstanceState, container);
    if(mVDB == null){
      return  null;
    } else {
      return mVDB.getRoot();
    }
  }

  @Override protected void onDestroy() {
    if(mVDB != null){
      mVDB.unbind();
    }

    super.onDestroy();
  }
}

package com.david.photoalbum.ui.activity;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import com.david.photoalbum.R;
import com.david.photoalbum.databinding.ActivityBaseBinding;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by david on 2017/3/16.
 */

public abstract class BaseActivity extends AppCompatActivity implements IActivity {

  private ActivityBaseBinding baseVDB;
  private Map<Integer, PermissionListener> permissionListenerMap = new HashMap<>();
  private AtomicInteger requestCodeNumber = new AtomicInteger(0);

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    /** 开始加载布局 **/
    baseVDB = DataBindingUtil.setContentView(this, R.layout.activity_base);
    View toolBarView = createToolBar(savedInstanceState, baseVDB.toolbarContainer);
    if (toolBarView != null) {
      baseVDB.toolbarContainer.addView(toolBarView);
    }
    View contentView = createContentView(savedInstanceState, baseVDB.contentContainer);
    if (contentView != null) {
      baseVDB.contentContainer.addView(contentView);
    }
    /** 加载布局结束 **/
    preInit(savedInstanceState);
    initView();
    initListener();
    initData();
  }

  /**
   * 在执行init(initView、initListener、initData)之前执行
   */
  protected void preInit(Bundle savedInstanceState) {

  }

  /**
   * 创建ToolBar的View
   */
  protected abstract View createToolBar(Bundle savedInstanceState, ViewGroup container);

  /**
   * 创建页面主要内容View
   */
  protected abstract View createContentView(Bundle savedInstanceState, ViewGroup container);

  public void requestPermission(String permisson, PermissionListener listener) {
    if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, permisson)) {
      if (listener != null) {
        listener.onGranted();
      }
    } else {
      boolean hasDenited = ActivityCompat.shouldShowRequestPermissionRationale(this, permisson);
      if (!hasDenited) {
        int requestCode = requestCodeNumber.getAndIncrement();
        permissionListenerMap.put(requestCode, listener);
        ActivityCompat.requestPermissions(this, new String[] { permisson }, requestCode);
      } else {
        if (listener != null) {
          listener.onNotAsk();
        }
      }
    }
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    int result = grantResults[0];
    PermissionListener listener = permissionListenerMap.get(requestCode);
    if (PackageManager.PERMISSION_GRANTED == result) {
      if (listener != null) {
        listener.onGranted();
      }
    } else {
      if (listener != null) {
        listener.onDenited();
      }
    }
    permissionListenerMap.remove(requestCode);
  }

  /**
   * 权限申请结果监听器
   */
  public interface PermissionListener {
    void onGranted();//权限请求被允许

    void onDenited();//权限请求被拒绝

    void onNotAsk();//不再请求
  }
}

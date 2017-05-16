package map.baidu.fusong.com.bdmapservice_pro.app;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;

import map.baidu.fusong.com.bdmapservice_pro.LocationServ.LocalServManager;

/**
 * Created by alin on 2017/5/15.
 * @description fosung.com.cn
 *  定位服务初始化在全局类 （根据情况调用服务管理类）
 */

public class ServApp extends Application {

    public LocalServManager locationService = null;

    @Override
    public void onCreate() {
        super.onCreate();
        locationService = new LocalServManager(getApplicationContext()); //管理百度地图的定位
       // mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
    }
}

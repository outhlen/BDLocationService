package map.baidu.fusong.com.bdmapservice_pro.LocationServ;

import android.content.Context;
import android.content.pm.LauncherApps;
import android.os.Handler;
import android.os.SystemClock;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;

import map.baidu.fusong.com.bdmapservice_pro.listener.LocationServCallBack;
import map.baidu.fusong.com.bdmapservice_pro.utils.MapUtils;

/**
 * Created by alin on 2017/5/15.
 * @description fosung.com.cn
 * 百度定位LBS
 */

public class LocalServManager implements BDLocationListener ,Runnable {

    private LocationClient client = null;
    private LocationClientOption mOption,DIYoption;
    private Object  objLock = new Object();
    private OnGetLocation  onGetLocation = null;
    /**
     * 定位超时时间
     */
    public static final int FAILURE_DELAYTIME = 12000;
    /**
     * 获取到的定位位置
     */
    public static BDLocation LOCATION;
    /**
     * 定位缓存时间  15秒
     */
    public static final int LOCATION_CACHE_TIME = 15000;

    /*上次定位时间，默认如果上次定位时间小于15秒，不再定位防止频繁定位*/
    private static long LAST_LOCATION_TIME;
    private BDLocation aMapLocation;                            // 用于判断定位超时
    private Handler handler = new Handler();
     private Context context;
    public LocationServCallBack localServCallback;

    @Override
    public void run() {
        if (aMapLocation == null) {
            //12秒内还没有定位成功，停止定位
            stopLocation();// 销毁掉定位
            if (onGetLocation != null) {
                onGetLocation.locationFail();
            }
        }
    }

    /**
     * 获取位置后回调
     */
    public interface OnGetLocation {
        void getLocation(BDLocation location);
        void locationFail();
    }
    /***
     * @param locationContext
     */

    public LocalServManager(Context locationContext){
        synchronized (objLock) {
            if(client == null){
                client = new LocationClient(locationContext);
                client.setLocOption(getDefaultLocationClientOption());
            }
        }
        this.context = locationContext;
    }

    /***
     * 订阅定位的监听
     * @param listener
     * @return boolean
     */

    public boolean registerListener(BDLocationListener listener){
        boolean isSuccess = false;
        if(listener != null){
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return  isSuccess;
    }

    /***
     * 取消定位的监听
     * @param listener
     * @return boolean
     */
    public void unregisterListener(BDLocationListener listener){
        if(listener != null){
            client.unRegisterLocationListener(listener);
        }
    }

    /***
     *  设置定位可选属性
     * @param option
     * @return isSuccessSetOption
     */
    public boolean setLocationOption(LocationClientOption option){
        boolean isSuccess = false;
        if(option != null){
            if(client.isStarted())
                client.stop();
            DIYoption = option;
            client.setLocOption(option);
        }
        return isSuccess;
    }

    public LocationClientOption getOption(){
        return DIYoption;
    }

    /***
     *  获取定位信息
     * @return DefaultLocationClientOption
     */

    public LocationClientOption getDefaultLocationClientOption(){
        if(mOption == null){
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            mOption.setScanSpan(3000);//可选，默认3000，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
            mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
            mOption.setNeedDeviceDirect(false);//可选，设置是否需要设备方向结果
            mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            mOption.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
            mOption.setIsNeedAltitude(false);//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        }
        return mOption;
    }

    /**
     * 开始定位（如果在LOCATION_CACHE_TIME之内，则获取缓存定位数据，否则进行定位）
     *
     * @param onGetLocation 定位完成回调接口
     */
    public void startOrGetCacheLocation(OnGetLocation onGetLocation) {
        this.onGetLocation = onGetLocation;
        if (LOCATION != null && LAST_LOCATION_TIME > 0 && SystemClock.elapsedRealtime() - LAST_LOCATION_TIME < LOCATION_CACHE_TIME) {
            if (onGetLocation != null) {
                onGetLocation.getLocation(LOCATION);
            }
        } else {
            startLocation();
        }
    }

    public void startLocation(){

        registerListener(this); //注册监听
        setLocationOption(getDefaultLocationClientOption()); //设置定位可选项
        synchronized (objLock) {
            if(client != null && !client.isStarted()){
                client.start();
            }
        }
        handler.postDelayed(this, FAILURE_DELAYTIME);// 设置超过12秒还没有定位到就停止定位
    }
    public void stopLocation(){
        synchronized (objLock) {
            if(client != null && client.isStarted()){
                client.stop();
            }
        }
        if (client != null) {
            client = null;
        }
    }
    public boolean requestHotSpotState(){
        return client.requestHotSpotState();
    }

    /**
     * 获取格式化好的距离
     *
     * @param lat1      纬度1
     * @param long1     经度1
     * @param lat2      纬度2
     * @param long2     经度2
     */
    public static String getFormatDistance(double lat1, double long1, double lat2, double long2) {

        LatLng location = new LatLng(lat1, long1);
        return MapUtils.getDistance(location, new LatLng(lat2, long2));
    }

    /**
     * 结束定位，连本次的回调任务也取消
     */
    public void stop() {
        handler.removeCallbacks(this);
        stopLocation();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location != null) {
            this.aMapLocation = location;// 判断超时机制
            if (onGetLocation != null) {
                if (location.getLatitude() == 0 && location.getLongitude() == 0) {
                    onGetLocation.locationFail();
                    LOCATION = null;
                } else {
                    onGetLocation.getLocation(location);
                    LOCATION = location;
                    LAST_LOCATION_TIME = SystemClock.elapsedRealtime();
                }
            }
            //得到地址后停止定位
            stop();
        }
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

}

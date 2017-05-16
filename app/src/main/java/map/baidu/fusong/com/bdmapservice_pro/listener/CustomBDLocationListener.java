package map.baidu.fusong.com.bdmapservice_pro.listener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;


/**
 * Created by alin on 2017/5/15.
 *
 * @description fosung.com.cn
 *
 * 接收定位返回的结果集 （按需要获取信息）
 */

public class CustomBDLocationListener implements CustomLocationListener {

    private Context mContext;
    public BDLocation localInfo;
    private LocationServCallBack mCallBack;

    public CustomBDLocationListener(Context ct) {
        this.mContext = ct;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {

        if (null != location && location.getLocType() != BDLocation.TypeServerError) {
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nlocType : ");// 定位类型
            sb.append(location.getLocType());
            sb.append("\nlocType description : ");// *****对应的定位类型说明*****
            sb.append(location.getLocTypeDescription());
            sb.append("\nlatitude : ");// 纬度
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");// 经度
            sb.append(location.getLongitude());
            Log.v("定位成功", "位置信息：" + sb.toString());
            Toast.makeText(mContext, "位置：" + sb.toString(), Toast.LENGTH_SHORT).show();

        }
    }
        @Override
        public void onConnectHotSpotMessage (String s,int i){
        }

        @Override
        public BDLocation getBDLocation () {
            return localInfo;
        }

}
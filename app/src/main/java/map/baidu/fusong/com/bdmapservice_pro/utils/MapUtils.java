package map.baidu.fusong.com.bdmapservice_pro.utils;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by alin on 2017/5/16.
 *
 * @description fosung.com.cn
 *
 * 计算辅助类
 */

public class MapUtils  {

    /**
     * 计算两点之间距离 double
     * @param start
     * @param end
     */
    public static String getDistance(LatLng start, LatLng end){
        //位置坐标经纬度
        double lat1 = (Math.PI/180)*start.latitude;
        double lat2 = (Math.PI/180)*end.latitude;
        double lon1 = (Math.PI/180)*start.longitude;
        double lon2 = (Math.PI/180)*end.longitude;
        //地球半径
        double R = 6371;
        //两点间距离 km，如果想要米的话，结果*1000
        double d =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*R;
        if(d<1)
            return (int)d*1000+"m";
        else
            return String.format("%.2f",d)+"km";
    }

}

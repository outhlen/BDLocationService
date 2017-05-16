package map.baidu.fusong.com.bdmapservice_pro.listener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;


/**
 * Created by alin on 2017/5/16.
 *
 * @description fosung.com.cn
 */

public interface CustomLocationListener extends BDLocationListener {

   public  BDLocation getBDLocation();

}

# 地图定位
百度地图定位 LocalServManager 辅助类 简单实现的位置定位计算 两点距离 后续功能陆续补充 

'locationService.startOrGetCacheLocation(new LocalServManager.OnGetLocation() {

                        @Override
                        public void getLocation(BDLocation location) {
                            Toast.makeText(ServActivity.this,"回调信息："+location.getCity(),Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void locationFail() {
                            Toast.makeText(ServActivity.this,"定位失败",Toast.LENGTH_SHORT).show();
                        }
                    });'

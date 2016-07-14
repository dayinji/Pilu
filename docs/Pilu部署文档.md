# Pilu部署文档

测试设备: 小米2S 
操作系统版本: Android 5.0


### With APK
APK下载链接：xxx
请直接下载安装即可运行。

### With Android Studio
1. 进入目标文件夹，用git将Pilu项目下载到本地。

	```bash
	$ cd <Your-Folder>
	$ git clone https://github.com/dayinji/Pilu.git
	```

2. 打开Android Studio，File -> Open，选择刚刚下载到本地的Pilu文件夹，点击OK，即完成导入。
	
	![](http://ww2.sinaimg.cn/large/6a3dd34djw1f5tgqaprv1j21kw0i3dju.jpg)￼

3. 连接Android设备，点击Run即可运行；选择设备时请选择与电脑连接的真机。
	![](http://ww3.sinaimg.cn/large/6a3dd34djw1f5tgolxv0dj21kw10cgqz.jpg)
	![](http://ww1.sinaimg.cn/large/6a3dd34djw1f5tgoo1f4ij21kw0zvai7.jpg)￼





### 注意事项
1. 与电脑连接时，手机上请点击“允许调试”
2. 安装的时候请使手机保持非锁屏状态
3. 安装过程请在手机上点击选择“继续安装”
4. 部分手机设置锁屏密码时会导致安装失败(报错为`INSTALL_CANCELLED_BY_USER`)，请去掉锁屏密码之后再作尝试
5. ……


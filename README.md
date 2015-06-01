# LTDemo
OpenGL  video  player
           为了能够采用硬件加速播放视频，和对视频一些渲染处理，弄了个Opengl的播放器。
   1.在GLSurfaceview中实现了播放，暂停，快进退等一些该有的功能。
        ex:屏幕左边上下滑调节亮度，右边调节音量，中间区域左右滑动快进退。
   2.有本地和网络播放两快，基本播放封装在BasePlayerActivity中。其他本地和网络的特地逻辑都是继承自BasePlayerActivity的
      LoacalPlayerActivity和NetPlayerActivity。
   3.因为只是写了个Demo所以就大概弄了点东西。具体可以下载代码来看看。
   
   具体图片可在项目跟路径下找那两张截图看看。LTDemo/screenshot1...
![image](https://github.com/happyzlg51/LTDemo/screenshot2.png)

光能蜗牛计划
之
Alice 计划

我们的口号是:技术温暖世界

目前唤醒词:
小爱小爱，小爱同学，光能蜗牛

目录说明
-float_manager 这个是悬浮窗模块
-uiasr 百度唤醒识别的uiasr模块
-core  这个百度唤醒识别core模块
-baidu_tts 这个是百度语音合成模块


**备注  百度唤醒识别 的集成过程


**备注  baidu_tts 的集成过程
-baidu_tts 的集成过程，考虑到tts sdk经常变动，所以这里直接将baidu的ttsDemo 拷贝过来，然后修改
apply plugin: 'com.android.application'
为
apply plugin: 'com.android.library'
并将 applicationId "com.baidu.tts.sample"注释掉
然后你发现这里面的部分activity会遇到R资源属性引用的问题，
因此，这里修改所有的跟switch语句改为if else语句

之后，尽量不更改百度本身的sdk的前提下，我们拷贝其中一个类SynthActivity并修改为MySynthActivity放到我们的主App中
然后修改其中的appid appkey 等等信息，完工
后续的修改，比如一些界面资源文件的更改之类，就可以考虑在主项目进行，不在影响百度本身的tts sdk
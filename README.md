![119691637202260_.pic_hd.jpg](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/48c5ccf66f4d40698e1d5231e0bba830~tplv-k3u1fbpfcp-watermark.image?)

# 一,环境
使用IntelliJ创建Compose Desktop项目,JDK这里选择11以上
![image.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/49636462ea724d3596d840a113a092a5~tplv-k3u1fbpfcp-watermark.image?)

然后点击“Next”按钮，这将会跳转至确认 Compose 模块的界面。现在点击“Finish”按钮，IntelliJ 将通过自动下载适当的 gradle 为你配置整个项目。
![image.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/f29a6af3ea9345dca148cf402b4fabc8~tplv-k3u1fbpfcp-watermark.image?)

运行compose桌面应用
![image.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/5125c3d2c13b4761a329c9b5c1804210~tplv-k3u1fbpfcp-watermark.image?)

运行显示如下
![image.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/de297c5e528f4c38bd7b4cc774a47dea~tplv-k3u1fbpfcp-watermark.image?)
# 二,原型图分析
如原型图所示,ui分为上下结构的设备状态,两个相机预览区域,和运行日志

上下布局采用Column,其中Modifier以高阶函数链式调用的形式支持Modifier.size().background().padding()等诸多属性,如下所示
```
Column(Modifier.background(MaterialTheme.colors.surface).fillMaxSize().padding(padding)) {

    //设备状态
    message(deviceStatus.value)

    //两个相机预览区域
    Row {
        cameraPreview(this@Window, mvCamreraPanel)
        cameraPreview(this@Window, javacvPanel)
    }

    //运行日志
    messageList(visible.value, data.value) { visible.value = !visible.value }
}
```
#### 设备状态实现

很简单采用横向控件Row,并设置垂直居中即可
```
@Composable
fun message(deviceStatus: DeviceStatus) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Text("设备状态: ${deviceStatus.deviceStatus}", modifier = Modifier.padding(end = padding), fontSize = fontSize)

        Text("相机状态: ${deviceStatus.cameraStatus}", modifier = Modifier.padding(end = padding), fontSize = fontSize)

        Text("联网状态: ${deviceStatus.netStatus}", fontSize = fontSize)
    }
}
```

#### 预览布局实现

其中SwingPanel是compose用来兼容swing的组件

```
@Composable
fun cameraPreview(frameWindowScope: FrameWindowScope, camreraPanel: CamreraPanel) {
    frameWindowScope.window.addWindowListener(object : WindowAdapter() {
        override fun windowClosing(e: WindowEvent) {
            camreraPanel.stop()
        }
    })

    val padding = if (camreraPanel is JavacvCameraPanel) 0.dp else padding
    Column(Modifier.padding(top = 5.dp).wrapContentWidth()) {
        cameraMessage("俯视机位预览") { camreraPanel.saveImage() }
        SwingPanel(
            modifier = Modifier.padding(top = 5.dp, end = padding)
                .size(Dp(previewWidth.toFloat()), Dp(previewHeight.toFloat())),
            factory = { camreraPanel.getPanel() })
    }
}
```

#### 滑动日志展示实现

主要采用LazyColumn,实现列表展示
```
@Composable
fun messageList(more: Boolean, messages: List<Message>, onClick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text("运行日志: ", modifier = Modifier.padding(top = 5.dp, bottom = 5.dp), fontSize = fontSize)
        TextButton(
            modifier = Modifier.size(50.dp, 30.dp),
            shape = RoundedCornerShape(50),
            contentPadding = PaddingValues(vertical = 3.dp),
            onClick = onClick,
        ) {
            val text = if (more) "放大" else "缩小"
            Text(text, fontSize = fontSize)
        }
    }
    LazyColumn(
        modifier = Modifier.border(1.dp, color = Color.Gray)
            .fillMaxSize()
            .padding(10.dp)
    ) {
        items(
            count = messages.size,
            key = { index -> messages[index] }
        ) { index ->
            Text(messages[index].message, fontSize = 12.sp)
        }
    }
}
```

# 三,相机预览分析
打开usb摄像头的库有[javacv](https://github.com/bytedeco/javacv) 和 [webcam-capture](https://github.com/sarxos/webcam-capture),

webcam-capture在windows上发现预览非常卡顿,webcam-capture对摄像头预览做了太多封装,具体问题就不分析了
```
@Composable
fun camera2Preview(frameWindowScope: FrameWindowScope) {
    val webcam = Webcam.getDefault()?.apply {
        setCustomViewSizes(Dimension(4000, 3000), Dimension(1920, 1080), Dimension(1280, 720))
        viewSize = WebcamResolution.FHD.size
    }

    frameWindowScope.window.addWindowListener(object : WindowAdapter() {
        override fun windowClosing(e: WindowEvent) {
            webcam?.close()
        }
    })

    Column(Modifier.padding(top = 5.dp).wrapContentWidth()) {
        cameraMessage("侧视机位预览") {
            CoroutineScope(Dispatchers.IO).launch {
                WebcamUtils.capture(webcam, System.currentTimeMillis().toString(), ImageUtils.FORMAT_PNG)
                JOptionPane.showMessageDialog(null, "拍照成功")
            }
        }
        SwingPanel(
            modifier = Modifier.padding(top = 5.dp, end = 15.dp).border(1.dp, color = Color.Gray)
                .size(Dp(previewWidth.toFloat()), Dp(previewHeight.toFloat())),
            factory = {
                WebcamPanel(webcam, true).apply {
                    isFPSDisplayed = true
                    isImageSizeDisplayed = true
                    isFPSDisplayed = true
                    drawMode = WebcamPanel.DrawMode.FILL
                }
            })
    }
}
```

使用javacv做相机预览,javacv提供了CanvasFrame作为一个窗口预览,只用采用while循环获取摄像头输出的每一帧canvas.showImage(grabber.grab())即可显示图像
```
OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(1);//新建opencv抓取器，一般的电脑和移动端设备中摄像头默认序号是0，不排除其他情况
grabber.start();//开始获取摄像头数据

CanvasFrame canvas = new CanvasFrame("摄像头预览");//新建一个预览窗口
canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//窗口是否关闭
while (canvas.isDisplayable()) {
    /*获取摄像头图像并在窗口中显示,这里Frame frame=grabber.grab()得到是解码后的视频图像*/
    canvas.showImage(grabber.grab());
}
grabber.close();//停止抓取
```
而我们需要将图像预览在compose desktop上,那么就需要grabber.grab()获取每一帧,显示在自己的控件上,
这里我采用的swing的`JLabel`显示图像,就需要用到SwingPanel了  
将Swing组件添加到你的Compose UI中，需要在`SwingPanel`部件的`factory` lambda参数中创建JComponent,示例如下:

```
SwingPanel(
    background = Color.White,
    modifier = Modifier.size(270.dp, 90.dp),
    factory = {
        JPanel().apply {
            setLayout(BoxLayout(this, BoxLayout.Y_AXIS))
            add(actionButton("1. Swing Button: decrement", dec))
            add(actionButton("2. Swing Button: decrement", dec))
            add(actionButton("3. Swing Button: decrement", dec))
        }
    }
)
```
现在可以实现摄像头预览框了:
```
@Composable
fun cvCameraPreview(frameWindowScope: FrameWindowScope) {
    val javacvPanel = JavacvCameraPanel()
    frameWindowScope.window.addWindowListener(object : WindowAdapter() {
        override fun windowClosing(e: WindowEvent) {
            javacvPanel.stop()
        }
    })

    Column(Modifier.padding(top = 5.dp).wrapContentWidth()) {
        cameraMessage("侧视机位预览") { javacvPanel.saveImage() }
        SwingPanel(
            modifier = Modifier.padding(top = 5.dp)
                .size(Dp(previewWidth.toFloat()), Dp(previewHeight.toFloat())),
            factory = {
                javacvPanel
            })
    }
}
```
其中JavacvCameraPanel为javacv预览的JLabel
```
class JavacvCameraPanel : JLabel(), CamreraPanel {

    private var job: Job? = null
    private var grabber: OpenCVFrameGrabber? = null

    init {
        addcvLine()
        start()
    }

    override fun getPanel() = this

    override fun start() {

        job = CoroutineScope(Dispatchers.IO).launch {

            tryCatchFor {

                grabber = OpenCVFrameGrabber.createDefault(0).apply {
                    imageWidth = pictureWidth
                    imageHeight = pictureHeight
                    start()
                }

                do {
                    getBufferedImage(grabber?.grab()).let {
                        icon = ImageIcon(createThumbnail(it, previewWidth, previewHeight))
                    }
                    // 每40毫秒刷新视频,一秒25帧
                    Thread.sleep(40)
                } while (isVisible && isActive && grabber != null)
            }
        }
    }
}
```

看看预览界面:

![image.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/e87b840dcef74510bf3c9a9df7c2f4dd~tplv-k3u1fbpfcp-watermark.image?)
其中还涉及到对每一帧图像做居中裁剪,否则预览就是1920*1080这样矩形预览框  
另一个相机预览展示同理,采用海康sdk获取摄像头预览每一帧,显示在JLabel

边框和虚线实现,由于也是第一次使用swing,查找api花了很久,其实继续添加JLabel就能实现
```
fun JLabel.addcvLine() {
    //边框
    add(JLabel("").apply {
        border = BorderFactory.createLineBorder(Color.BLACK)
        setBounds(0, 0, previewWidth, previewHeight)
    }, BorderLayout.CENTER)

    //横线
    add(JLabel("").apply {
        border = compoundBorder()
        setBounds(0, previewHeight / 2, previewWidth, 1)
    }, BorderLayout.CENTER)

    //竖线
    add(JLabel("").apply {
        border = compoundBorder()
        setBounds(previewWidth / 2, 0, 1, previewHeight)
    }, BorderLayout.CENTER)
}
```

#### 实现效果如下
![image.png](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/09d81c2f14404dd288ceb04ef2963718~tplv-k3u1fbpfcp-watermark.image?)
# 四,总结
在compose之前,声明式ui在flutter和swift中尝试过,所以画简单的ui没什么特别大感觉  
踩坑:  
1,mac下打包不支持打包exe文件,必须使用windows才可以  
2,对摄像头曝光,缩放等在mac设置无效,换到windows上面就可以  
3,compose desktop没有对桌面应用升级的支持,加上javacv打包出来800M+  
4,似乎不支持设置文本复制?

[git地址](https://github.com/luxiao0314/camera-compose-desktop)

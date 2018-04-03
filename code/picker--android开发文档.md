#Picker开发档案

by  [ay27](https://github.com/ay27)

##目录
1. [整体框架](#1)
2. [module类](#2)
3. [数据库支持](#3)
4. [服务器连接](#4)
5. [数据上传和文件下载](#5)
6. [一些界面支持模块](#6)
7. [Material Design的支持](#7)
8. [登录和注册](#8)
9. [书本管理](#9)
10. [添加书本](#10)
11. [用户信息](#11)
12. [圈子（暂时没有开放此功能，但是已经开发）](#12)
13. [书本内容](#13)
14. [页码扫描和搜索](#14)
15. [问题内容](#15)
16. [笔记内容](#16)
17. [资料内容](#17)
18. [新建内容](#18)
19. [裁剪和纠偏](#19)
20. [总结](#20)
21. [附：项目配置](#21)


<h2 id=1>整体框架</h2>

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/Blank Flowchart - New Page.png)


<h2 id=2>module类</h2>
* module的结构
* 各module类描述

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午4.10.18.png)


###module的结构
由于服务器使用了Spring的MVC结构，为了与服务器保持简单的同步，android端也使用了类似的MVC结构。也就是说，module这个包里的内容应该是与服务器一样。同时，为了简化android端的设计，添加了几个接口，它们分别是：

* IsFavorite，可以点赞（问题，笔记，回答等）
* IsFollow，可以关注（问题，笔记等）
* IsJoinable，可以加入（圈子）

同时为了简化上传的工作，设定了几个上传用的form：

* AnswerForm，上传回答
* AttachmentFeedForm，上传附件贴
* CommentForm，上传评论
* FeedForm，上传问题和笔记
* LoginForm和RegisterForm，如名

同时，为了便于展示（如书本的问题列表，需要获取到这个问题列表中各问题的用户头像，对应的需要服务器查询两张表），减少与服务器的交互，同时为了保持与服务器一致，设定了一些Display类，可以直接查看源码得知具体功能。

另外，为了便于android端数据库的设计，所有的module都继承自`BaseModule类`，因为把一条信息插入数据库时，需要提供一个`id`，为了插入时消除重复，直接使用来自服务器的id，而不是自己生成。

###各module类描述
1. Answer，也即回答，这里有两个变量值得关注：`questionId`和`replierId`，分别是这个回答对应的`问题id`和`回答者id`。用于获得回答者信息的类是`AnswerDP`
2. AttachmentFile，附件贴中的文件，里边的`path`是这个文件的服务器路径，`aFeedId`是这个文件对应的附件贴id
3. Book，其中的`hasInventory`属性是获知这本书是否存在章节，是后期才加上的，暂时没有使用，但其实在问题列表里是需要用到的
4. Circle，圈子，暂时没有开放的功能，有点像QQ群的概念，但是感觉不够完善，所以暂时没有开放。`establisherID`是这个圈子的建立者id，`describe`是这个圈子的宣言
5. Comment，评论，`commentedId`对应的可能是问题的id，也可能是笔记的id，或者附件贴的id
6. Favorite，用于点赞，`objectId`是对应的被点赞对象
7. Feed，是问题，笔记和附件贴的集合。里边有对应的`Type`描述，`sectionStr`忘了干什么用了，暂时没有使用到。。。 `brief`用于在查看书本的Feed列表时展示出简略信息
8. Follow，关注
9. Message，消息，用户间的相互点赞，关注，问题，回答等都会产生对应的一条条消息，然后推送到个人那里，以及推送到关注他的人那里。这里边有大量的`MessageType`，这是为了android和web的展示。例如：`MESSAGE_FOLLOWED_ASKQUESTION`表示`我关注的XX问了一个问题`，`MESSAGE_YOUR_QUESTION_FAVORITED`表示`你的问题被点赞了`
10. PrivateLetter，表示私信中的某一条信息。其中的`dialogId`是这个私信对话的id
11. PrivateMessageSum，表示一个私信，里边包含多条`PrivateLetter`，如果你问为什么不叫`Dialog`，那是因为李明隆的英文太渣
12. SearchResult，表示搜索出来的某一条数据。里边有6种结果，但是我们只需要用一个image，一个content以及一个name即可表示，具体可以查看`search_result_item`这个layout以及`SearchResultAdapter`
13. Section，用于章节展示。这里是递归定义的。比如说，第一章是一个section，第一章第二节也是一个section，但是第一章第二节是包含在第一章中的。所以使用一个`subSections`的list来表示这种关系。
14. User，用户。里边有一个用户的所有信息。

稍后会解释这里的module和数据库的关系。



<h2 id=3>数据库支持</h2>
* 使用数据库的原因
* 数据库支持的详细描述

###使用数据库的原因
一开始想着用数据库来进行一些数据的缓存，后来发现，缓存的必要性不大，随后改造成适于列表ListView的展示，因为系统为我们提供了一个`CursorAdapter`，我们把数据从服务器中查询过来，然后插入数据库中，随后由系统自动通知CursorAdapter来进行页面更新。但是由于CursorAdapter的定制性实在差强人意，在某些界面还是使用了传统的BaseAdapter。之后会看到这些界面的。

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午4.59.30.png)

这是数据库支持的主要部件。在view/templete/CursorSupport里有列表界面相关的设置，这里一并讲述。

其实到后来会发现，使用数据库来这样做实在是比较糟糕的实现方法，性能还不如直接用BaseAdapter。所以到后来确实有过把这些需要数据库支持的界面全部改成不需要数据库的支持，不过实在没有时间了，就让后来的人完成吧。

###数据库支持的详细描述
数据库在android上是以一个`ContentProvider`内容提供者的角色存在。也即，我们设计了一个ContentProvider供自己使用。

下面是Picker中数据库的一些要点：

####AndroidManifest中ContentProvider的定义

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午5.10.15.png)


####PickerContentProvider的设计
每一个ContentProvider都需要实现`增删改查`几个基本功能。

* `ContentProvider`用来提供数据给Application，然后在Application中使用`ContentResolver`来连接`ContentProvider`，实现了两者的解耦
* 为了能够根据Uri来判断是哪个数据表，实现了一个`UriMatcher`
* 为了与数据库关联，需要一个`SQLiteOpenHelper`，在Picker中是`InnerHelper`，InnerHelper只需要实现数据库的建立和更新即可
* 为了实现数据库的数据更新能够通知到界面并使界面做出相应改变，需要构建一整条通知链，具体可以参考我的博客：<http://www.ay27.pw/blog/2014/08/26/a-simple-demo/>
* Picker数据库的最基本定义在`DataBase`类中
* `DataTable类`包含了需要用到的所有数据表，InnerHelper会根据这里的数据表定义而建立数据库
* 每个数据表只保存以下数据： 1.id  2.服务器返回的json数据  3.页码page  4.时间time； 可以在DataTable中的`sql_createTable`看到这些定义
* 在数据库中保存json数据而不是每个module的具体信息的理由：使用Gson库可以非常完美的实现module和json之间的转换，那么就没有多少必要实现如此复杂的数据表。我们仅需在数据进出数据库的关口用Gson实现转换
* `DataHelper类`用于向外暴露数据库接口，它需要使用`ContentResolver`来调用到`PickerContentProvider`的接口

####CursorSupport的设计要点
* `CursorSupport`类的工作原理是：把一个CursorAdapter绑定到某个界面的LoadManager，然后给manager注册一个`Callback`，用来自动触发CursorAdapter的更新
* 由于这个LoadManager可能来自`Activity`，也可能来自`Fragment`，所以代码里有`两个版本`的Constructor和Callback
* CursorSupport使用`DataHelper`实现了数据库的插入，更新和清空，其他的数据库功能如果需要开放，可以在这里完成
* 数据库的操作需要异步完成


<h2 id=4>服务器连接</h2>
* 连接的方法
* 各支持类的描述


![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午6.06.47.png)

###连接的方法
使用了volley库来简化开发。 与服务器的交互主要是两种方法：post和get方法。

###各支持类的描述
* `PickerRequest类`是Picker于volley的代理接口，所有的request类都是继承自PickerRequest。在这个类里实现了：1.把任务添加到volley的工作队列；2.默认的ErrorCallback；3.volley的一些参数设置
* `PostRequest类`，用于上传，有一个代理Callback，用于将json转换成各种Status。Status的具体定义在`Status类`中
* `ModuleQueryRequest类`和`ListModuleQueryRequest类`，一个是实现了查询module，一个实现了查询一个list。这两者的差异是返回来的json解析方法不一样。具体可以参考源码
* `Urls类`，这里定义了服务器的地址，以及各种连接返回来json格式后，用于解析数据用的key。但是其实这些key应该与每个url对应，而不是目前的状态。
* `UrlGenerator类`，用于提供各种服务器功能的url。每个url会有对应的key用于解析返回的json数据。但是由于开发初期的结构问题，使得这里的key和url分开两地。现在也完全可以把两者合并，但是完全是一些重复简单的动作。就让后来者完成吧
* `RequestManager类`，用于把任务压入volley的处理队列中


<h2 id=5>数据上传和文件下载</h2>
* 简述
* 上传和下载模块的结构

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午6.47.31.png)

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午6.48.34.png)

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午6.51.26.png)

###简述
（由于某些原因，上传和下载分布在三个地方。而且这个模块的实现非常糟糕，结构有点混乱，希望后来者对此有所改善）

数据上传包括图片上传和文件上传，文件下载仅仅包括了文件下载，图片直接使用`ImageCacheManager类`来实现加载。

图片上传在writer/upload/FeedTask中，使用了upload_download/MyUploadManager。

###upload_download
（这个模块推荐使用service进行重构，方法见：<http://blog.csdn.net/liuhe688/article/details/6623924>）

首先讲述upload_download里边的内容，这是这几个类的关系图：

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/diagram.png)

* `MyDownloadManager`使用了系统的DownloadManager，但是发现下载服务器的文件时总是出错，后来改成了直接使用HTTPConnection来实行下载，具体可以看到`view/attachment/AttachmentActivity/FileDownLoader`这个类，也就是说MyDownloadManager暂时是废弃了的（但是经过测试，下载一些网上的资源文件是可行的，估计原因应该是服务器的设置有问题）
* `MyUploadManager`是在`UploadTask`的外边包了一层，主要是建立了一个上载队列
* `BroadcastNode`以及`S_P_Callback`两个共同构建起一个`订阅者--发布者`结构，S也即subscribe（订阅），P也即publisher（发布者）。`BroadcastNode`是订阅者和发布者的管理节点。构建这一结构是出于这样的考虑：
>
我们在当前界面点击了下载，下载在后台运行着；此刻，我们可以切换到下载管理界面，就可以看到所有的正在下载项目（需要显示下载进度）、已经下载成功的项目等。发现了没有，我们在点击下载的那一刻，在当前界面是无法把消息投递到一个尚未构建的Activity中的，是无法在下载建立的时候把监听器从下载管理界面中注册到下载器中的。上载也是同样的道理。

* 于是，我们需要一个消息的中转站，这个中转站可以暂存消息，可以接受到访者的访问，可以接受投递者投递消息。当然，由于开发时间限制，这个中转站尚未投入使用，但是经过了测试，是可行的一个方案
* 同时需要注意一个关键点：中转站还需要实时的获取到投递者的状态，是开启状态还是完成状态，或是错误状态。同时需要关注到访者的到访时刻。有可能出现这样的情况：到访者拿着投递者的id来询问投递者的相关状态，然后中转站告知投递者正在运行；然后到访者就开始访问投递者。由于是完全异步执行的，有可能在询问的过程中，投递者刚好完成了任务，但是还没有通知到中转站更新状态，然后到访者就傻傻地在那等着投递者的消息。 在目前的结构中这种可能是存在的，但是尚未测试出来。然后由于时间关系，这部分就这样先用着了。。。后来者可以在这里做一些改进
* 注意我们的上传模块是使用了iso-8859-1编码，不是utf-8，具体原因是服务器，貌似是tomcat默认是iso-8859-1

###view/writer/upload
![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-19 下午5.53.14.png)

先说明几个类完成的工作：

* AttachmentTask，FeedTask：一个是用于上传附件贴，一个是用于上传问题和笔记
* ImageUploadUtils：上传图片的一些支持
* Uploader：外部将任务投递到这里，然后在这里分配任务

下面针对几个类说明一下类内部各属性就清楚了。

####AttachmentTask

    private String title;	//附件贴的题目
    private String rawData;	//附件贴的md文本
    private long bookId = -1;		//附件贴对应的书本id
    private int bookPage = 0;		//对应的书本页码
    private ArrayList<Uri> files;	//一个附件贴可以附带多个文件
    private UploadCallback callback; //上传的消息监听器
    private volatile double delta = 0, progressNum = 0; //delta是每个部分上传所占的百分比，progressNum是指示当前上传的进度
    private MyUploadManager uploadManager = MyUploadManager.getInstance();	//上传工具
    private Semaphore uploadAttachments, uploadContent;  //uploadAttachments是上传文件锁，唯有所有文件上传成功，服务器返回了对应的id后，这个锁才解开；uploadContent需要等到所有的文件上传完毕并获得了服务器返回的文件id后，才开始上传整个AttachmentFeedForm，唯有等到所有的数据上传完毕后，uploadContent锁解开，然后向外部投递上传完毕的消息    
    private ArrayList<Integer> attachmentIds;  //把文件上传到服务器后，服务器返回对应的id，这些id需要写入到AttachmentFeedForm，完成文件与附件贴的对应

####FeedTask实现类似，不再赘述。

####ImageUploadUtils
上传图片的过程与上传文件的过程类似，也是先把图片上传到服务器的临时目录，然后服务器返回图片对应的id，然后，此时会有所不同：

* 本地拿到图片的id后，把md文本中图片的对应文本替换掉，以这样的格式替换：`![...](file://id)`
* 把md文本上传到服务器后，服务器根据图片的md格式进行扫描，然后把对应id的图片从临时目录挪动到固定目录，然后用服务器的地址替换掉图片的链接。至此，图片上传完毕


<h2 id=6>一些界面支持模块</h2>
* 整体描述
* 各模块描述

###模块整体描述
![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午7.10.22.png)

界面支持的代码量非常大，使用了很多额外的库来实现比较好的界面。在Picker的众多Module（这里的Module是相对于Project来说的）中，只有cropper不是用于实现界面的。
下面简述各模块的细节。

###各模块描述
####AndroidBootstrap
库的源地址：<https://github.com/Bearded-Hen/Android-Bootstrap/>

这个模块是一些Boostrap风格的控件，以及Bookstrap风格的图标。

这里的图标用了Font-Answome库<http://fortawesome.github.io/Font-Awesome/cheatsheet/>。使用方法如下：

```
<com.beardedhen.androidbootstrap.FontAwesomeText 
	android:layout_width="wrap_content"
	android:layout_height="wrap_content"
	android:textSize="10sp"
	fontawesometext:fa_icon="fa-thumbs-up"
	android:textColor="@color/favorite_item_color_default"
	android:id="@+id/_favorite_icon"
/>
```

可以看到，只需要改变fontawesometext:fa_icon属性就可以改变图标的样式，颜色等直接当成是普通的文字处理即可，非常方便。

####appcompat
直接拷贝自`android-sdk/extras/android/support/v7/appcompat`，是为了使用Material Design风格才用上的。如果使用gradle编译，这一个库是可以省去的，但是picker使用了ant编译，这里需要拷贝过来才能编译。实际在代码的唯一用处就是定义Material Design的style

####EditTextValidator
用于提供以下界面，详细用法见源码：

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午3.41.37.png)

####PickerWidget
这里包含了一些简单的控件，以及某些我个人设计的控件。

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午7.39.43.png)

下面只是简单的描述各个控件的用途，更具体的看源码。

1. markdown_support：如名，提供了一个MarkdownView，用于把markdown文本展示才胡来
2. preference_support：因为andorid系统不存在`PreferenceFragment`，所以只能自造一个，用于`设置`界面
3. AdjustableListView：为了解决在`ScrollView`中ListView的高度计算问题（这应该算是android系统的bug）
4. BookPageIndicator：用于实现这个控件：![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午3.45.38.png)
5. CardsAnimationAdapter：用于列表的弹出动画
6. FitSystemWindowFrameLayout：为了解决使用了沉浸式界面后，底部的输入栏不能随软键盘的弹出而自动跟随。可见：<https://code.google.com/p/android/issues/detail?id=63777>，以及<http://stackoverflow.com/questions/21092888/windowsoftinputmode-adjustresize-not-working-with-translucent-action-navbar>
7. FloatingButton：这玩意是可以跟随ListView的滚动而弹出或收回滴：![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/picker3.png)
8. NumberAdjustableTextView：一个数字展示TexView，根据提供的数字，而自动调整展示的文本，使得在一个相当小的地方展示比较大的数字。如：提供`12345`，会展示出`1.2w`
9. SeparatorBar：一个分隔条，可以带上文字，具体看使用的地方
10. SuperFavoriteButton：用于点赞的按钮控件，某些功能在Picker/view/widget中实现

####pinned_section_listview
用于实现在问题列表按章节排序后，列表的可固定效果
![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午9.11.11.png)

####supertoasts
有几种风格，慢慢体会：

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午9.13.50.png)

####额外的模块
一个是位于`Picker/swipe_back`，用于实现右滑返回。放在Picker里边是因为需要用到`PickerApplication`中的一些数据。**注意**：

1. 这个模块的适配性感觉有点问题，在我的miui6和android5.0上能够完美实现功能，但是在某些系统中右滑时底下是黑色而不是前一个界面的图。希望后来者可以完美解决这个问题
2. 注意在右滑时需要看到底下前一个界面的图，需要在style.xml中添加这个标签：`<item name="android:windowIsTranslucent">true</item>`
3. 在右滑的最后一个界面（这个界面是固定不能滑的），这个最后的界面虽然不能滑动，但是还是需要继承自`SwipeBackActivity`，但是需要在`onCreate`中添上：

```
getSwipeBackLayout().setEnabled(false);
getSwipeBackLayout().setEdgeSize(0);
```
更具体的可以查看`view/MainActivity`

另外一个是`view/widget`下的三个Button：

* FavoriteButton
* FollowButton
* JoinButton

具体直接看源码。

<h2 id=7>Material Design的支持</h2>
* 简述
* 实现

###简述
使用`MaterialDesign`带来了最主要的功能是App顶部的`Toolbar`。Toolbar的特色，一个是`书本界面`左上角的`drawer动画`，另一个是在`FeedActivity`中天衣无缝的`TabSelector`和Toolbar的融合。当然还有其他自动替换的一些更美观的控件。

###实现
使用MaterialDesign其实不难。要使用MaterialDesign只需做以下工作：

1. 添加`appcompat`，在前面已经说过
2. 更改style，使之继承自`Theme.AppCompat.Light`，具体见源码（注意：`windowTranslucentStatus`
和`windowTranslucentNavigation`的设置是为了顶部栏的沉浸式风格）。style里边的属性见这幅图：![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/Component-Theme1.png)
3. 更改`AndroidManifest`文件中的`Application Theme`
4. 在每一个需要使用Toolbar的界面中把Toolbar当成一个普通控件添上
5. 把Activity改成继承自`ActionBarActivity`，然后在`onCreate`调用`setSupportActionBar(toolbar)`

更具体的信息可以看`MainActivity`中的实现方法。

<h2 id=8>登录和注册</h2>
此部分比较简单，下面简述一下几个实现细节即可。
###1. Cookie的设定（无论是SignIn还是SignUp，都需要设定，不设定的话服务器无法识别用户是否登录）

```
CookieManager cookieManager = new CookieManager();
CookieHandler.setDefault(cookieManager);
```

###2. 登录流程
分两步：登录 ---> 查询该用户信息（主要是获取用户的id）

###3. SignActivity中使用了RadioGroup+ViewPager实现
这里需要注意`OnPageChangeListener`中的`onPageScrollStateChanged`回调函数。为了实现与直接使用系统Tab的效果，需要如Picker源码那样的做法。如果在`onPageSelected`触发时就改变RadioGroup的选中状态，会使得滑动手感很差，不信的话可以尝试一下就知道我说的意思了。


<h2 id=9>书本管理</h2>
![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午3.42.15.png)

在这个部分，讲述`MainActivity类`及该界面下侧边栏的一些情况，但是侧边栏上的用户信息一栏在[用户信息](#13)部分讲述，因为那一部分比较大，所以分割开。

* 书本 --- BookFragment
* 关注 --- MessageFragment
* 消息 --- MessageFragment
* 私信 --- DialogFragment
* 下载 --- FileFragment
* 搜索 --- SearchFragment
* 设置 --- Preferences

####书本
在这个界面中，可以看到我关于数据库和列表界面（AbsListView）是如何关联的。注意这里没有使用`CursorSupport类`，是想着把我最原始的写法展现出来，`CursorSupport`是根据这个原始写法改进的。

####关注和消息
可以看到，在这两个界面中都是使用了同一个Fragment：MessageFragment。注意到这里直接继承了`ListSupportFragment`，相比BookFragment省去了不少的代码。`ListSupportFragment`会在稍后的[书本内容](#13)讲述。

####私信
这里列出了所有的私信对象。继承自`SwipeListFragment`（可下拉刷新的ListView），同样在稍后的[书本内容](#13)讲述。

这里直接把由DialogFragment引导去的`PrivateLetterActivity`讲述清楚。可以清楚的看到，在`PrivateLetterActivity`里边只做了一件事，把`PrivateLetterFragment`引用进来做界面。而`PrivateLetterFragment`才是我们真正的私信对话界面。

为什么要这么做呢？这是由之前描述过的一个系统bug引起的。看下面两个界面，可以看到输入框是跟随软键盘的弹出收回而改变位置的。

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午3.43.50.png)
![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午3.43.57.png)

如果不使用MaterialDesign和沉浸式状态栏，那么可以直接使用我博客里写的做法：<http://www.ay27.pw/blog/2014/09/10/a_simple_problem_in_android/>

但是使用后发现无论如何都没法做到这个效果。后来查询后发现是系统的一个bug，这个bug已经在前面给出过链接了（这个bug不知道是否会在android5.0中出现，没有做相应的测试）。

现在描述解决这个问题的几个要点：

* 界面任然是按照最外层是`RelativeLayout`，其他控件的相对位置由底部的可变化栏决定。这里的界面不能加入`Toolbar`。这个界面是属于Fragment而不是Activity
* 属于Activity的界面是`fragment_container`，这里加入了Toolbar以及`FitSystemWindowFrameLayout`。如果直接使用系统的FrameLayout，会导致Fragment和Activity不能完全嵌合，会有一道空白，高度与状态栏一致。后来者可以尝试看看效果
* 在Activity中直接引用目标fragment即可，所有的工作在fragment中完成

在`PrivateLetterFragment`中可以看到，并没有使用数据库做数据中转。这里是因为几个原因：

1. 使用CursorAdapter后条目的顺序总是有问题，使用BaseAdapter没有这样的问题
2. 使用CursorAdapter时也是根据条目是发自自己还是对方而使用不同的Item界面，但是CursorAdapter本身的机制问题，导致这个Item界面经常错乱
3. 不使用CursorAdapter后，数据库就没必要使用了

####下载
下载界面仅仅是把Picker/download_files/中的文件展示出来而已，后续可以做很多工作，比如文件管理的一些操作

####搜索
这里的搜索和后边[页码扫描和搜索](#14)不是一个东西。后边的主要是使用页码进行搜索，这里是直接文本模糊搜索。可以看到可搜索的内容有这些：

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午3.44.14.png)

但是由于使用了MaterialDesign后，这个Spinner的下拉图标变白色了，导致一开始看不出来是一个下拉菜单，但是应该是可以改进的。

####设置
这里的设置是使用了前面说到过的`preference_support`里的东西，具体看源码。

<h2 id=10>添加书本</h2>
添加书本，目前只做了扫描ISBN后直接得出结果的做法，当然也可以直接在前面说到过的搜索界面中搜索到书本信息后添加。这里主要讲述扫描界面，书本信息界面没多少好说的。

###使用了ZXing库
这个库已经包含在了`libs/ImageProc_Module`库中，这部分是由李佳阳完成并交给我的。由于这个库还包含了后边用到的页码识别（Tesseract库），所以使用`ConcreteImageTool`做了一个中间层。

**必须注意到，所有的图像处理都需要谨慎仔细，一个是性能问题，一个是防止内存泄露！**

这里给出ScanActivity的流程图：

需要注意到以下几点：

* 摄像头的`PreviewSize`一定要设置正确，使之与屏幕分辨率一致或宽高比例一样，否则会产生拉伸变形
* `onPreviewFrame`中，可以看到每隔5帧图像才使用一帧，这是因为获取到图像后需要做两件事：1. 旋转图像； 2. 识别条码。这两个处理都是需要消耗大量资源的，唯有尽可能减少数据量才能完成
* 使用了一个线程池来完成没一帧需要处理的图像的旋转。旋转是因为：无论我们怎么设置摄像头，都别妄想能在`onPreviewFrame`中拿到竖屏方向的图像。当然设置摄像头的方向可以使得`拍照`时获取到正确方向的图像，但是实时获取摄像头数据时是不可能做到的，只能自己做一次旋转
* 关于旋转，可以参考我的一篇博文：<http://www.ay27.pw/blog/2014/10/09/a-simple-image-rotate-function/>
* 这里没有真正的做分辨率的适配，因为手头上没有更多的设备来测试，而虚拟机的摄像头是没法用的，即使能开启，由于无法对焦，图像也是几乎无法识别的。希望后来的人可以做好这点


<h2 id=11>用户信息</h2>
讲述这个界面以及这里点击后转跳过去的界面：

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午3.42.25.png)

这是对应的一些子界面：

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-18 上午9.45.10.png)

这些子界面都是直接使用了`ListSupportActivity`，可以看到代码量非常少

<h2 id=12>圈子（暂时没有开放此功能，但是已经开发）</h2>
这个功能，一开始是想着做成类似豆瓣读书小组一样的存在，或者类似QQ群。但是后来发现这个功能有点鸡肋，所有就屏蔽了。如果要开启，只需要在`MainActivity`中的侧边栏添加一栏即可。


<h2 id=13>书本内容</h2>
* FeedActivity中的一些细节
* QuestionList的两种排序方式
* template模板方法

###FeedActivity中的一些细节
![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午3.44.39.png)

讲述两个要点：一个是顶部的Tab栏，一个是浮动按钮。

####顶部Tab栏
由于使用了Toolbar，Toolbar不能使用原来的Tab in ActionBar的做法，而且原来的做法不支持更多的定制，所以就直接使用RadioGroup做了一个。关于RadioGroup和ViewPager的联合使用，在[登录和注册](#8)时已经说过，这里不再重复。

但是仍然有一个要讲述的是问题Tab的点击后弹出选择窗的功能。

* 弹出窗是一个`PopupMenu`
* 至于那个问题Tab上的角标，是直接使用了一个背景图片来完成的，使得用户感觉到这里有一个下拉菜单
* 界面滑动时，Tab栏底下有一个白色条跟随，是`FragmentIndicator类`的工作
* 更多的细节可以看这里：<http://www.ay27.pw/blog/2014/12/19/android-action-bar-tab/>

####浮动按钮
浮动按钮可以做到Fragment中，也可以做到Activity。这里选择做到Activity是因为三个fragment都需要用到。

* 使用`attachToListView`可以使得这个浮动按钮关联到某一个列表，使之随之滚动而弹出或收回
* 在`onPageSelected`调用上面的`attachToListView`
* 在`QuestionFragmentInTimeLine`这个Fragment的构建时传入浮动按钮，是因为一开始界面建立时不会触发onPageSelected，而且在Activity中不知道Fragment中的listView何时构建出来，所有只能把FloatingButton传入，让Fragment决定何时绑定

###QuestionList的两种排序方式
由于韩老师一定要添上`按章节排序`的功能，而我本人一直想坚持使用`按时间排序`，两相争执下，就把两种排序方式放在了一起，但是只实现在QuestionList中，先试着使用后，再决定笔记和附件是否也需要这样的排序方式。

（说明一点：按时间排序，其实是按问题的id排序，但是这个id是按时间线增长的，所以间接的变成了按时间排序。但是貌似由于CursorAdapter的排序问题，感觉顺序有点不靠谱，之后最好把CursorAdapter取缔）

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午3.44.49.png)

我们直接讲述实现比较麻烦的按章节排序。下面是目录选择器的几个要点：

* 目录选择器是放在FeedActivity中的`showChapterSelector`函数中实现，因为它本来是属于Tab的东西，不属于Fragment
* 目录选择器的adapter是`view/adapter/SectionAdapter`，注意到目录本身是递归定义的，从服务器拉取到的是最外层的一个`List<Section>`，我们需要把这个List解构平铺，然后把它们一一添加到需要展示的ItemList中
* 小节比章缩进是在直接在前面添加空格来实现的
* 目录选择器点击后直接调用对应的QuestionFragment中的`fastScroll`来进行快速定位

至于按章节排序后，需要一个pinned listview来进行指示目前的位置。也就是下面的界面：

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-17 下午9.11.11.png)

具体实现在`QuestionAdapterWithPinnedSection`中，这里讲述几个要点：

* Adapter需要实现一个接口：`PinnedSectionListAdapter`，用以判断当前的item是否需要固定
* 把所有条目按页码排序，然后从头到尾扫描一遍，有相同章节的就放在同一个章节中，否则设置一个新的章节指示条
* 递归扫描后拼接出章节指示条的文本：`concatSectionStr`

###template模板方法
主要讲述这个目录下的实现：

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-19 下午12.24.13.png)

模板方法：简单地说，就是父类中定好一个实现的流程，并实现了多个实现中的共同部分，关键步骤由子类实现

####1. ListSupportFragment
这个类比较关键，其他的模板基本上是由这个类演化过去的。下面的是比较完整的流程图

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/ListSupportTemplate.png)

一些比较关键的信息由子类实现的`getChildParameters`中获得。
下面详细描述`Parameters`中的属性：

```
private ListView listView;
private CursorAdapter adapter;
private View view;
private Type typeOfListModule;
private String listQueryUrl, listJsonKey;
private DataTable table;
```

* ListView和View：这两个是为了保证ListSupportFragment的可定制性。如某个界面是包含除了ListView的其他控件，那么可以向父类提交这个布局对应的View和ListView。如果不提供这两个参数，则父类自动初始化一个仅仅包含了一个ListView的界面。详细可见`onStep1SetUpListView`这个函数的实现
* CursorAdapter：提供一个特定的Adapter，用以实现ListView中的每一个item。这个Adapter必须由子类提供，因为这是一个ListView区别于其他ListView的关键部件
* typeOfListModule：提供用于获取到服务器json数据后，使用Gson解析时需要提供的Type定义，也是必须由子类提供
* listQueryUrl, listJsonKey：用于查询这个List的Url和用于解析获取到的json数据时，需要提供的一个JsonKey，也是由子类提供
* DataTable：由于是使用的CursorAdapter，所以需要对应的一个数据表。可以看到，数据库的支持直接使用了`CursorSupport`

####2. SwipeListFragment
直接继承自ListSupportFragment，但是增加了下拉刷新的功能，很简单，不再描述了

####3. ListPartialLoadFragment
与ListSupportFragment几乎一样，不一样的地方是，这里的列表是可以上拉到最后，自动加载更多。而ListSupportFragment中的List是直接加载全部。相应的添加了一个`loadMore`函数，用于实现上拉到底部后自动加载更多。直接看源码即可，没有多少技术

####4. ListSupportActivity
![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/listSupportActivity.png)

与ListSupportFragment类似，但是多了一个Module支持。是考虑到有些界面，会同时存在module和list，如QuestionContentActivity，这个界面既需要展示Question这个module，同时也需要展示对应的answer list。

而Parameters与之前的也有些不同，主要是多了Module的支持，具体看代码。

<h2 id=14>页码扫描和搜索</h2>
* 页码识别模块
* 搜索过程

声明：页码扫描说实话个人感觉实用性一般，当然这里是一个引子，方便日后添加文字识别功能

###页码识别模块
识别模块使用的是`Tesseract`库，采用的是其中的英文字母识别训练文件。代码基本集中在`view/writer/page_detector/PageDetectorActivity`这个类中。与ISBN码识别的代码几乎是一样的，除了一条调用`ImageProc`进行识别的代码不一样。

必须说明的是，几乎所有的模式识别算法都需要一个初始的训练文件，Tesseract也不例外。而且必须知道，这个训练文件一定要放在app能访问到的地方。

因此，我们选择的做法是：

1. 把这个训练文件放在`/assets/`目录下
2. 由于打包apk实际上是打包成一个zip压缩包，因此我们需要把这个训练文件自动的拷贝到app能访问到的位置。具体代码看：`PickerApplication`下的`checkImageModuleFile`函数
3. 拷贝到手机的这个位置：`sd卡/Picker/tesseract/tessdata/`
4. 看到`ConcreteImageTool类`，这里是与ImageProc库的一个代理接口，我们需要利用之前拷贝后的训练文件初始化`Tesseract`
5. `ConcreteImageProc`初始化时传入的两个参数是训练文件路径以及训练文件的语言格式，具体有什么训练文件，可以查看这个链接：<https://code.google.com/p/tesseract-ocr/downloads/list>  注意是下载`traineddata`文件
6. 需要知道的是，识别的准确度其实不可能达到100%，所以设定了一个可选列表，把识别到的数字显示出来，让用户自行选择
7. 我们选择的库是英文库，因为没有纯粹的数字库，所以我们选了一个较小的库，然后在识别时发现识别出字母后自动抛弃（开发时我们没有抛弃掉，然后发现识别到的几乎都是字母）

###搜索过程
注意到`PageDetectorActivity`仅仅进行了数字识别，识别到的结果返回到原来的Activity，然后进行下一步的搜索。具体的可以查看view/feed/SearchActivity 以及 view/feed/SearchResultFragment


<h2 id=15>问题内容</h2>
`QuestionContentActivity`，直接继承自`ListSupportActivity`，实现简单，不再赘述。

<h2 id=16>笔记内容</h2>
`NoteContentActivity`，因为有软键盘跟随的一个输入框，所以需要使用到前面说到的，解决那个bug的方法处理。

<h2 id=17>资料内容</h2>
没啥好讲的

<h2 id=18>新建内容</h2>
编辑界面由3个Fragment组成：ChooseBookPageFragment，UploadAttachmentFragment，WriteContentFragment。功能如名字。

需要说明一下，在UploadAttachmentFragment添加文件时，由于需要从一个Uri中提取出文件路径和文件名，但是由于Uri的编码和系统文件名的编码不一样，所以需要`Uri.decode(fileName)`来进行编码转换。具体代码在`comman/Utils/getPathFromUri`

这里需要说一下编辑器实现的一些细节，特别是插入图片和插入链接的实现方法：

* 插入图片是使用`ImageSpan`实现，在`insertPic`函数中，这个图片对应的文本是：`"<image>" + picUri.toString() + "</image>"`
* 注意到`start2ShowActivity`里边的`intent.putExtra("Content", WriteParser.text2Show(getActivity(), content.getText()))`这一句代码。这里把前面插入的图片、链接转换成标准的md格式文本
* 使用正则表达式来寻找所有的图片，然后替换成标准的md格式
* 转换后的md文本会送入到ShowActivity（预览），然后上传
* 上传完后，由服务器完成md文件的修改，因为需要由服务器生成对应的图片路径，然后嵌入到md文本中

<h2 id=19>裁剪和纠偏</h2>
`CropActivity`只是裁剪，`CutImageActivity`是裁剪+纠偏。CropActivity是使用了Crop库，CutImageAvtivity的具体实现见我的博客：<http://www.ay27.pw/blog/2014/08/04/imitation-scan-almighty-king-cropping-interface-implementation/>

<h2 id=20>总结</h2>
基本上差不多。

说来惭愧，一直想对项目做完整的单元测试，可是一直没能把单元测试的环境搭建出来。希望后来者可以做到这一点，把整个项目纳入到比较合理的测试当中。


<h2 id=21>附：项目配置</h2>
SDK版本：21

项目文件编码：UTF-8

picker使用了根目录下的picker.jks进行签名，密码是：bitman

初始版本没有进行混淆

因为使用了butterknife库进行一些界面的注入，所以需要设置java的注入器。注入选项可以选择自动识别，如果发现编译成功，但是运行app后直接抛错，就把它改成是指定的butterknife库。

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-19 下午5.10.52.png)

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-19 下午5.12.46.png)


每个模块的配置图：

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-19 下午5.14.53.png)

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-19 下午5.04.29.png)

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-19 下午5.04.39.png)

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-19 下午5.04.45.png)

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-19 下午5.04.50.png)

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-19 下午5.04.55.png)

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-19 下午5.05.04.png)

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-19 下午5.05.10.png)

![image](https://github.com/ay27/Picker/blob/master/code/screen_shot/屏幕快照 2015-01-19 下午5.05.16.png)


附带所有引用的项目的链接：

<https://github.com/JakeWharton/butterknife>

<https://github.com/Bearded-Hen/Android-Bootstrap>

<https://github.com/edmodo/cropper>

<https://github.com/throrin19/Android-Validator>

<https://github.com/JohnPersano/SuperToasts>

<https://github.com/nhaarman/ListViewAnimations>

<https://github.com/beworker/pinned-section-listview>

<https://github.com/falnatsheh/MarkdownView>

<https://code.google.com/p/google-gson/>

<https://code.google.com/p/tesseract-ocr/>

<https://github.com/zxing/zxing/>

<https://android.googlesource.com/platform/frameworks/volley>



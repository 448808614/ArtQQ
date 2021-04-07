# ToolQ

ArtQQ是一个基于QQ8.4.5的QQ协议框架，对外框架名为ToolQ

使用前，请打开AndroidInfo.java修改数据文件保存位置

> 2020.8.10
>**ArtQQ**独立开源

> 2020.8.22
> **ArtQQ**对外更名ToolQ
>
> 官方框架发布中心：toolq.cn
>
> 插件交流共享：qqkit.net

toolQ是由acgp团队研发的QQ机器人内核，你也可以叫她兔Q。

### 本项目将使用阿里规范作为开发规范
安装阿里规范的方式如下：
#### 在线安装：  


 打开IDEA，点击File->Settings->Plugins

 搜索Alibaba Java Coding Guidelines点击install进行安装


#### 离线安装：  


 下载地址：https://plugins.jetbrains.com/plugin/10046-alibaba-java-coding-guidelines

 打开IDEA,File->Settings->Plugin->Install plugin from disk(全英文路径)


#### 使用方法
（以上两步都需要重启Idea生效）
打开IDEA，点击tools-->安装的阿里编码规约，可以选择中英文切换，项目右键选择编码规约扫描就可以进行查看自己编码哪些地方不够好，进行修改哦。
#### 遵循规范
发现代码飘黄或者飘红波浪线， 把鼠标放上去，查看修正发生！
#### 常见问题
如果存在中文乱码问题：
可以Appearance&Behavior -> Appearance -> UI Options -> Name 里面设置成微软雅黑（microsoft yahei light）或者自己喜欢的其他字体哦。

### 代码追溯
如果你会提交代码，那么请你必须完成以下操作。  


 打开IDEA,File->Settings->Editor->File and Code Templates

 对Class、interface、Enum等文件在#parse("File Header.java")的下面加入：

/**
* @date ${YEAR}-${MONTH}-${DAY} ${HOUR}:${MINUTE}
* @author 名字
* ${description}
*/


> 关于代码规范设置

1. 取消关于魔法值的检测
2. 取消对代码行数超过80行检测
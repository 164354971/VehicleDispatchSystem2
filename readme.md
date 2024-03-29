## 首页展示
- 背景图轮播展示
- 根据需要选择租车校区、租车时间段
- 上拉刷新、下拉加载更多（根据每日车辆浏览量推荐）

<img src="https://lingbao-crisps.cn/static/readme/sy.png" width="220">

## 首页未登录提示窗
- 根据登录状态展示登录提示窗，选择登录方式

<img src="https://lingbao-crisps.cn/static/readme/dltsc.png" width="220">

## 品牌页展示
- 可根据首字母或品牌名称搜索品牌

<img src="https://lingbao-crisps.cn/static/readme/pp.png" width="220">

## （__新增__）消息页展示
- 轮询接收登录本机的用户消息（概览信息）
- 信息展示：用户信息、消息内容、接收信息时间

<img src="https://lingbao-crisps.cn/static/readme/xxy.png" width="220">

## 个人主页展示
- 个人主页展示：用户基本信息、用户订单模块、更多模块（待开发）

<img src="https://lingbao-crisps.cn/static/readme/wd.png" width="220">

## 车辆挑选页（首页->车辆挑选页）
- 根据首页筛选条件进入车辆挑选页，展示校区可用停车场与对应时间段内可租车辆
- 车辆展示：可根据车辆标签与价格进行车辆筛选与车辆排序

<img src="https://lingbao-crisps.cn/static/readme/tx.png" width="220">

- 停车场可展开选择其他停车场，点击停车场可展示停车场与本地位置

<img src="https://lingbao-crisps.cn/static/readme/txtcc.png" width="220">
<img src="https://lingbao-crisps.cn/static/readme/txtccwz.png" width="220">

## 车辆详情页（首页->车辆挑选页->车辆详情页）
- 车辆详情页展示车辆信息、图片、标签和价格；展示用户可用优惠券，计算实付金额

<img src="https://lingbao-crisps.cn/static/readme/clxq.png" width="220">

## 登录索引页（首页->车辆挑选页->车辆详情页->登录索引页）
- 权限访问：若用户未登录将跳转至登录索引页进行登录操作，登录成功后重新跳转

<img src="https://lingbao-crisps.cn/static/readme/dltzy.png" width="220">

## 车辆支付页（首页->车辆挑选页->车辆详情页->车辆支付页）
- 展示实付金额
- 选择支付方式（微信、支付宝、银行卡、余额）

<img src="https://lingbao-crisps.cn/static/readme/zfxz.png" width="220">

## 支付确认（首页->车辆挑选页->车辆详情页->车辆支付页->支付确认）
- 支付确认，确认后生成待支付订单

<img src="https://lingbao-crisps.cn/static/readme/zfqr.png" width="220">

## 支付密码输入页（首页->车辆挑选页->车辆详情页->车辆支付页->支付确认->支付密码输入页）
- 隐藏输入支付密码，确认正确后支付成功
- 密码输入错误三次订单自动取消
- 支付成功后生成支付单，更新订单支付状态

<img src="https://lingbao-crisps.cn/static/readme/zfmm.png" width="220">

## 取车二维码
- 车辆订单支付成功后生成取车二维码（动态更新），停车场管理员扫码确认取车

<img src="https://lingbao-crisps.cn/static/readme/qcewm.png" width="220">

## 型号页展示（品牌页->型号页）
- 根据品牌查询系统中已录取的车辆型号
- 展示该品牌中的热门评价及评分
- 车型可分类展示

<img src="https://lingbao-crisps.cn/static/readme/xh.png" width="220">

## 品牌概览展示（品牌页->型号页->品牌概览展示）
- 展示该品牌更多的热门评价

<img src="https://lingbao-crisps.cn/static/readme/ppjs.png" width="220">

## 品牌热门评价列表（品牌页->型号页->热门评价列表）
- 展示该品牌更多的热门评价

<img src="https://lingbao-crisps.cn/static/readme/pppl.png" width="220">

- 可根据评价查询更多回复，以及进行回复

<img src="https://lingbao-crisps.cn/static/readme/plhf.png" width="220">

## (__新增__)用户查询页（消息页->用户查询页）
- 根据用户昵称或账号模糊查询得到用户列表

<img src="https://lingbao-crisps.cn/static/readme/yhcx.png" width="220">

## (__新增__)聊天页（消息页->用户查询页->聊天页）
1. 双方用户在线、通过websocket实时通信
2. 对方用户无论是否在线，聊天详细数据都将通过rabbitmq异步更新到MySQL（详细聊天数据），聊天概览数据通过rabbitmq异步更新到redis（消息页轮询得到新的消息与提示）
3. 进入聊天页读取最近100条聊天记录并展示

<img src="https://lingbao-crisps.cn/static/readme/lty.png" width="220">
<img src="https://lingbao-crisps.cn/static/readme/lty2.png" width="220">

## 订单页展示（我的->订单页）
- 展示用户订单列表及其基本信息、提车时间段，可选择订单状态进行筛选
- 点击提车按钮可展示动态二维码

<img src="https://lingbao-crisps.cn/static/readme/dd.png" width="220">








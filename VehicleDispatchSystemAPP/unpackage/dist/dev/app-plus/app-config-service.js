
var isReady=false;var onReadyCallbacks=[];
var isServiceReady=false;var onServiceReadyCallbacks=[];
var __uniConfig = {"pages":["pages/index/index","pages/login/login","pages/login/login_index","pages/home/home","pages/select/select","pages/demo/demo","pages/brand/brand","pages/chat/chat","pages/chat/demo","pages/chat/chat_details","pages/model/model","pages/car/car","pages/navigation/navigation","pages/details/details","pages/map/map","pages/details/details_order","pages/order/obligation_list","pages/order/in_progress_list","pages/order/complete_list","pages/order/cancel_list","pages/order/order","components/payPasswordPopUp","pages/details/details_qrcode"],"window":{"navigationBarTextStyle":"black","navigationBarTitleText":"uni-app","navigationBarBackgroundColor":"#F8F8F8","backgroundColor":"#F8F8F8","enablePullDownRefresh":true},"tabBar":{"borderStyle":"black","selectedColor":"#0055ff","color":"#444444","list":[{"pagePath":"pages/index/index","iconPath":"static/tabbar/index_hidden.png","selectedIconPath":"static/tabbar/index_selected.png","text":"首页"},{"pagePath":"pages/brand/brand","iconPath":"static/tabbar/car_hidden.png","selectedIconPath":"static/tabbar/car_selected.png","text":"选车"},{"pagePath":"pages/chat/chat","iconPath":"static/tabbar/chat_hidden.png","selectedIconPath":"static/tabbar/chat_selected.png","text":"消息"},{"pagePath":"pages/home/home","iconPath":"static/tabbar/home_hidden.png","selectedIconPath":"static/tabbar/home_selected.png","text":"我的"}]},"darkmode":false,"nvueCompiler":"uni-app","nvueStyleCompiler":"uni-app","renderer":"auto","splashscreen":{"alwaysShowBeforeRender":true,"autoclose":false},"appname":"VehicleDispatchSystemAPP","compilerVersion":"3.99","entryPagePath":"pages/index/index","networkTimeout":{"request":60000,"connectSocket":60000,"uploadFile":60000,"downloadFile":60000}};
var __uniRoutes = [{"path":"/pages/index/index","meta":{"isQuit":true,"isTabBar":true},"window":{"navigationBarTitleText":"","navigationStyle":"custom","onReachBottonDistance":-100,"enablePullDownRefresh":true,"titleView":false}},{"path":"/pages/login/login","meta":{},"window":{"navigationBarTitleText":"","navigationStyle":"custom","titleView":false}},{"path":"/pages/login/login_index","meta":{},"window":{"navigationBarTitleText":"","navigationStyle":"custom","titleView":false}},{"path":"/pages/home/home","meta":{"isQuit":true,"isTabBar":true},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false,"navigationStyle":"custom","titleView":false}},{"path":"/pages/select/select","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false,"onReachBottonDistance":-100}},{"path":"/pages/demo/demo","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false}},{"path":"/pages/brand/brand","meta":{"isQuit":true,"isTabBar":true},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false,"navigationStyle":"custom","titleView":false}},{"path":"/pages/chat/chat","meta":{"isQuit":true,"isTabBar":true},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false,"navigationStyle":"custom","titleView":false}},{"path":"/pages/chat/demo","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false,"navigationStyle":"custom","titleView":false}},{"path":"/pages/chat/chat_details","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false,"navigationStyle":"custom","titleView":false}},{"path":"/pages/model/model","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false,"navigationStyle":"custom","titleView":false,"softinputMode":"adjustResize"}},{"path":"/pages/car/car","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false,"navigationStyle":"custom","titleView":false}},{"path":"/pages/navigation/navigation","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false}},{"path":"/pages/details/details","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false}},{"path":"/pages/map/map","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false}},{"path":"/pages/details/details_order","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false}},{"path":"/pages/order/obligation_list","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false,"navigationStyle":"custom","titleView":false}},{"path":"/pages/order/in_progress_list","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false,"navigationStyle":"custom","titleView":false}},{"path":"/pages/order/complete_list","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false,"navigationStyle":"custom","titleView":false}},{"path":"/pages/order/cancel_list","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false,"navigationStyle":"custom","titleView":false}},{"path":"/pages/order/order","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false,"navigationStyle":"custom","titleView":false}},{"path":"/components/payPasswordPopUp","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false}},{"path":"/pages/details/details_qrcode","meta":{},"window":{"navigationBarTitleText":"","enablePullDownRefresh":false}}];
__uniConfig.onReady=function(callback){if(__uniConfig.ready){callback()}else{onReadyCallbacks.push(callback)}};Object.defineProperty(__uniConfig,"ready",{get:function(){return isReady},set:function(val){isReady=val;if(!isReady){return}const callbacks=onReadyCallbacks.slice(0);onReadyCallbacks.length=0;callbacks.forEach(function(callback){callback()})}});
__uniConfig.onServiceReady=function(callback){if(__uniConfig.serviceReady){callback()}else{onServiceReadyCallbacks.push(callback)}};Object.defineProperty(__uniConfig,"serviceReady",{get:function(){return isServiceReady},set:function(val){isServiceReady=val;if(!isServiceReady){return}const callbacks=onServiceReadyCallbacks.slice(0);onServiceReadyCallbacks.length=0;callbacks.forEach(function(callback){callback()})}});
service.register("uni-app-config",{create(a,b,c){if(!__uniConfig.viewport){var d=b.weex.config.env.scale,e=b.weex.config.env.deviceWidth,f=Math.ceil(e/d);Object.assign(__uniConfig,{viewport:f,defaultFontSize:Math.round(f/20)})}return{instance:{__uniConfig:__uniConfig,__uniRoutes:__uniRoutes,global:void 0,window:void 0,document:void 0,frames:void 0,self:void 0,location:void 0,navigator:void 0,localStorage:void 0,history:void 0,Caches:void 0,screen:void 0,alert:void 0,confirm:void 0,prompt:void 0,fetch:void 0,XMLHttpRequest:void 0,WebSocket:void 0,webkit:void 0,print:void 0}}}});

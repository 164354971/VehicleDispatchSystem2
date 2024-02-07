package cn.lingbaocrisps.websocket.constants;

public interface MessageType {
    String MESSAGE_LOGIN_SUCCESS="1"; //登录成功
    String MESSAGE_LOGIN_FAIL="2"; //登录失败
    String MESSAGE_COMM_MES="3";//普通信息包
    String MESSAGE_GET_ONLINE_FRIEND="4"; //得到在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND="5";//返回在线用户列表
    String MESSAGE_CLIENT_EXIT="6"; //客户请求退出
    String MESSAGE_CLIENT_NO_EXIST="7"; //发送目标不存在
    String MESSAGE_CLIENT_OFFLINE="8"; //发送目标不存在
}

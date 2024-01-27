package love.lingbao.user.utils;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class SendMsgUtils {
    // AppId  1400开头的
    @Value("${txsms.AppId}")
    private int AppId;
    // 短信应用SDK AppKey
    @Value("${txsms.AppKey}")
    private String AppKey;
    // 短信模板ID
    @Value("${txsms.TemplateId}")
    private int TemplateId;
    // 签名
    @Value("${txsms.signName}")
    private String signName;

    /**
     * 腾讯云短信,100条一个月
     * 方法说明
     *
     * @param phone 用户的手机号码
     * @return void
     * @Discription:扩展说明
     * @throws HTTPException  http status exception
     * @throws IOException    network problem
     */
    public static SmsSingleSenderResult sendMsgByTxPlatform(String phone, String[] params) throws Exception {

        // 短信应用SDK AppID
        // 1400开头
        int appId = 1400818305;

        // 短信应用SDK AppKey
        String appKey = "6780714c3b3b5ce01ddcdd13645f9c20";

        // 短信模板ID，需要在短信应用中申请
        //NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
        int templateId = 1865590;

        // 签名
        // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
        String smsSign = "玲宝的练习空间网";

        SmsSingleSender sSender = new SmsSingleSender(appId, appKey);
        //第一个参数0表示普通短信,1表示营销短信

        /*
        @param nationCode 国家码，如 86 为中国
        @param phoneNumber 不带国家码的手机号
        @param templateId 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
        @param params 信息内容，必须与申请的模板格式一致，否则将返回错误，{}占位符可在代码中用实际需要发送的值替换
        @param sign 签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
        @param extend 扩展码，可填空
        @param ext 服务端原样返回的参数，可填空
        */

        SmsSingleSenderResult result = sSender.sendWithParam("86", phone,
                templateId, params, smsSign, "", ""); // 签名参数未提供或者为空时，会使用默认签名发送短信

        return result;
    }

}

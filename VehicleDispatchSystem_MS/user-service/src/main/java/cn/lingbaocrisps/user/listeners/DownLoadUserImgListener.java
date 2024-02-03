package cn.lingbaocrisps.user.listeners;

import cn.hutool.core.lang.UUID;
import cn.lingbaocrisps.common.domain.PathConstants;
import cn.lingbaocrisps.user.domain.bo.UserImgBO;
import cn.lingbaocrisps.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Component
@Slf4j
@RequiredArgsConstructor
public class DownLoadUserImgListener {

    private final IUserService userService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "mark.user.img.queue", durable = "true"),
            exchange = @Exchange(value = "user.topic", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "user.success"
    ))
    public void downLoadUserImg(UserImgBO userImgBO) {
        log.info("监听到图片下载 ：{}" ,userImgBO);
        String imgName = userImgBO.getUsername() + ".jpg";
        //下载图片
        String path = PathConstants.USER_IMG_PATH + imgName;

        try {
            FileUtils.copyURLToFile(new URL(userImgBO.getImgUrl()), new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

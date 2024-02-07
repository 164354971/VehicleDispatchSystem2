package cn.lingbaocrisps.websocket.controller;

import cn.lingbaocrisps.common.domain.R;
import cn.lingbaocrisps.websocket.domain.bo.MessageBO;
import cn.lingbaocrisps.websocket.domain.vo.MessageVO;
import cn.lingbaocrisps.websocket.domain.vo.SimpleMessageVO;
import cn.lingbaocrisps.websocket.service.IMessageService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "消息模块")
@RestController("/message")
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/messages")
public class MessageController {

    private final IMessageService messageService;

    @GetMapping("/{senderId}/{getterId}")
    public R<List<MessageVO>> getMessageList(@PathVariable Integer senderId, @PathVariable Integer getterId) {
        return R.ok(messageService.getMessageList(senderId, getterId));
    }

    @GetMapping("/update/{senderId}/{getterId}")
    public R<Void> updateMessageList(@PathVariable Integer senderId, @PathVariable Integer getterId) {
        messageService.updateSimpleMessageNoticeContentNum(senderId, getterId);
        return R.ok();
    }

    @GetMapping
    public R<List<SimpleMessageVO>> getSimpleMessageList() {
        return R.ok(messageService.getSimpleMessageList());
    }


}

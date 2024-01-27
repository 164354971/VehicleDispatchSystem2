package love.lingbao.trade.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.lingbao.common.domain.R;
import love.lingbao.trade.domain.dto.OrderFormDTO;
import love.lingbao.trade.domain.vo.OrderVO;
import love.lingbao.trade.service.IOrderService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户订单模块")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/orders")
public class OrderController {

    private final IOrderService orderService;

    @ApiOperation("生成订单接口")
    @PostMapping
    public R<OrderVO> createOrder(@RequestBody @Validated OrderFormDTO orderFormDTO){
        log.info("/orders post -> createOrder: " +
                "orderFormDTO = {}; 订单生成", orderFormDTO);
        return R.ok(orderService.createOrder(orderFormDTO));
    }

    @ApiOperation("取消订单接口")
    @GetMapping("/{orderId}")
    public R<Void> cancelOrder(@PathVariable Long orderId){
        log.info("/orders/{orderId} get -> cancelOrder: " +
                "orderId = {}; 取消订单", orderId);
        orderService.cancelOrder(orderId);
        return R.ok();
    }
}

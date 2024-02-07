package cn.lingbaocrisps.pay.controller;

import cn.lingbaocrisps.pay.domain.dto.PayApplyDTO;
import cn.lingbaocrisps.pay.enums.PayType;
import cn.lingbaocrisps.pay.service.IPayOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.lingbaocrisps.common.domain.R;
import cn.lingbaocrisps.common.exception.BizIllegalException;
import org.springframework.web.bind.annotation.*;

@Api(tags = "支付模块")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pays")
public class PayController {

    private final IPayOrderService payOrderService;

    @ApiOperation("生成支付单")
    @PostMapping
    public R<String> applyPayOrder(@RequestBody PayApplyDTO applyDTO){
        if(!PayType.BALANCE.equalsValue(applyDTO.getPaymentType())){
            // 目前只支持余额支付
            throw new BizIllegalException("抱歉，目前只支持余额支付");
        }
        return R.ok(payOrderService.applyPayOrder(applyDTO));
    }

    /**
     * 根据订单号查询支付表的支付状态
     * @param orderNo 订单号
     * @return 支付状态
     * */
    @ApiOperation("根据订单号查询支付表的支付状态")
    @GetMapping("/{orderNo}")
    Integer getPaymentStatus(@PathVariable("orderNo") Long orderNo){
        return payOrderService.getPaymentStatus(orderNo);
    }

}

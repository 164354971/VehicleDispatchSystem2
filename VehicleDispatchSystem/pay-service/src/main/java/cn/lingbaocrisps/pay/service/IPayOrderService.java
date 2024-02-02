package cn.lingbaocrisps.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.lingbaocrisps.pay.domain.dto.PayApplyDTO;
import cn.lingbaocrisps.pay.domain.po.PayOrder;

public interface IPayOrderService extends IService<PayOrder> {

    String applyPayOrder(PayApplyDTO payApplyDTO);

    Integer getPaymentStatus(Long orderNo);
}

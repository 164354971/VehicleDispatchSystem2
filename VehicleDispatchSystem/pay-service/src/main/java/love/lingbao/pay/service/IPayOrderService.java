package love.lingbao.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.lingbao.pay.domain.dto.PayApplyDTO;
import love.lingbao.pay.domain.po.PayOrder;

public interface IPayOrderService extends IService<PayOrder> {

    String applyPayOrder(PayApplyDTO payApplyDTO);

    Integer getPaymentStatus(Long orderNo);
}

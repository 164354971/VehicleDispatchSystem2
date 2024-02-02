package cn.lingbaocrisps.pay.service.impl;

import cn.lingbaocrisps.pay.domain.dto.PayApplyDTO;
import cn.lingbaocrisps.pay.domain.po.PayOrder;
import cn.lingbaocrisps.pay.mapper.PayOrderMapper;
import cn.lingbaocrisps.pay.service.IPayOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import cn.lingbaocrisps.common.domain.RedisConstants;
import cn.lingbaocrisps.common.exception.BadRequestException;
import cn.lingbaocrisps.common.utils.RedisTools;
import cn.lingbaocrisps.common.utils.UserContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {

    private final RedisTools redisTools;
    @Override
    @GlobalTransactional
    public String applyPayOrder(PayApplyDTO payApplyDTO) {
        Integer userId = UserContext.getUser();
        //业务订单id不能为空
        Long orderNo = payApplyDTO.getOrderNo();
        if(orderNo == null)
            throw new BadRequestException("业务订单id不能为空");
        //业务订单id去redis中查询是否存在，不存在意味着订单已过期
        String orderJsonString = redisTools.get(RedisConstants.ORDER_KEY + orderNo, String.class);
        if(orderJsonString == null)
            throw new BadRequestException("业务订单已过期");

        //
        redisTools.delete(RedisConstants.ORDER_KEY + orderNo);
        return null;
    }

    @Override
    public Integer getPaymentStatus(Long orderNo) {
        PayOrder payOrder = lambdaQuery().eq(PayOrder::getOrderNo, orderNo).one();
        if(payOrder == null)
            return null;
        return payOrder.getPaymentStatus();
    }
}

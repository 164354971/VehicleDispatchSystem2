package love.lingbao.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import love.lingbao.common.domain.RedisConstants;
import love.lingbao.common.exception.BadRequestException;
import love.lingbao.common.utils.RedisTools;
import love.lingbao.common.utils.UserContext;
import love.lingbao.pay.domain.dto.PayApplyDTO;
import love.lingbao.pay.domain.po.PayOrder;
import love.lingbao.pay.mapper.PayOrderMapper;
import love.lingbao.pay.service.IPayOrderService;
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

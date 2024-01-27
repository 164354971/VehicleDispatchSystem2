package love.lingbao.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.lingbao.trade.domain.dto.OrderFormDTO;
import love.lingbao.trade.domain.po.Order;
import love.lingbao.trade.domain.vo.OrderVO;

public interface IOrderService extends IService<Order> {

    OrderVO createOrder(OrderFormDTO orderFormDTO);

    void cancelOrder(Long orderId);

    void markOrderPaySuccess(Long orderId);
}

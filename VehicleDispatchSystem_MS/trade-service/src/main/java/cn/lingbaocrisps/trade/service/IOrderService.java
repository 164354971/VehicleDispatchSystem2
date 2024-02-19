package cn.lingbaocrisps.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.lingbaocrisps.trade.domain.dto.OrderFormDTO;
import cn.lingbaocrisps.trade.domain.po.Order;
import cn.lingbaocrisps.trade.domain.vo.OrderVO;

public interface IOrderService extends IService<Order> {

    OrderVO createOrder(OrderFormDTO orderFormDTO);

    void cancelOrder(Long orderId);

    void markOrderPaySuccess(Long orderId);
}

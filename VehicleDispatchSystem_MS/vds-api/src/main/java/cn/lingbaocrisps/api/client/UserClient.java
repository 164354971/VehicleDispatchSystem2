package cn.lingbaocrisps.api.client;

import cn.lingbaocrisps.common.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(value = "vds-user-service")
public interface UserClient {

    /**
    * 查询用户状态
    * */
    @GetMapping("/users/status/{userId}")
    R<Integer> getUserStatus(@PathVariable Integer userId);

    /**
     * 查询用户会员等级
     * */
    @GetMapping("/users/vip/{userId}")
    Integer getUserVipLevel(@PathVariable Integer userId);

    /**
     * 查询用户头像
     * */
    @GetMapping("/users/img/{userId}")
    String getUserImg(@PathVariable Integer userId);


}

package love.lingbao.api.client;

import love.lingbao.common.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}

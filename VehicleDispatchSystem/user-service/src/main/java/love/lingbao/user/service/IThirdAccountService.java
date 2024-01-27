package love.lingbao.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.lingbao.user.domain.dto.ThirdAccountDTO;
import love.lingbao.user.domain.po.ThirdAccount;
import love.lingbao.user.domain.vo.UserVO;

public interface IThirdAccountService extends IService<ThirdAccount> {

    UserVO thirdAccountLogin(ThirdAccountDTO thirdAccountDTO);
}

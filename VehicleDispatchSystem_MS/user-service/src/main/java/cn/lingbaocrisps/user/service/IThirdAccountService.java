package cn.lingbaocrisps.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.lingbaocrisps.user.domain.dto.ThirdAccountDTO;
import cn.lingbaocrisps.user.domain.po.ThirdAccount;
import cn.lingbaocrisps.user.domain.vo.UserVO;

public interface IThirdAccountService extends IService<ThirdAccount> {

    UserVO thirdAccountLogin(ThirdAccountDTO thirdAccountDTO);
}

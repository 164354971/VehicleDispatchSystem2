package love.lingbao.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.lingbao.user.domain.dto.LoginFormDTO;
import love.lingbao.user.domain.dto.RegisterFormDTO;
import love.lingbao.user.domain.dto.ThirdAccountDTO;
import love.lingbao.user.domain.po.User;
import love.lingbao.user.domain.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IUserService extends IService<User> {

    UserVO login(LoginFormDTO loginFormDTO, HttpServletRequest httpServletRequest);


    String createToken(Long userId);

    void getImageCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    UserVO register(RegisterFormDTO registerFormDTO);

    void getPhoneCode(String phoneNumber);

    User getUser(Integer userId);
    Integer getUserStatus(Long userId);
}

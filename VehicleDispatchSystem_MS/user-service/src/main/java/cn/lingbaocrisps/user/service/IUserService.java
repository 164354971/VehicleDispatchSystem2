package cn.lingbaocrisps.user.service;

import cn.lingbaocrisps.user.domain.dto.LoginFormDTO;
import cn.lingbaocrisps.user.domain.dto.RegisterFormDTO;
import cn.lingbaocrisps.user.domain.po.User;
import cn.lingbaocrisps.user.domain.vo.OtherVO;
import cn.lingbaocrisps.user.domain.vo.UserVO;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IUserService extends IService<User> {

    UserVO login(LoginFormDTO loginFormDTO, HttpServletRequest httpServletRequest);


    String createToken(Integer userId);

    String getImageCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    UserVO register(RegisterFormDTO registerFormDTO);

    void getPhoneCode(String phoneNumber);

    User getUser(Integer userId);
    Integer getUserStatus(Integer userId);

    String getUserImg(Integer userId);

    List<OtherVO> getChatUserList(String str);
}

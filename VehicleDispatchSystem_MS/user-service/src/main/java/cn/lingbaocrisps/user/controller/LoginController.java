package cn.lingbaocrisps.user.controller;

import cn.lingbaocrisps.common.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.lingbaocrisps.common.domain.R;
import cn.lingbaocrisps.user.domain.dto.LoginFormDTO;
import cn.lingbaocrisps.user.domain.dto.ThirdAccountDTO;
import cn.lingbaocrisps.user.domain.vo.UserVO;
import cn.lingbaocrisps.user.service.IThirdAccountService;
import cn.lingbaocrisps.user.service.IUserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;

@Api(tags = "登录相关接口")
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor  //必备参数变量初始化
@Slf4j
public class LoginController {

    private final IUserService userService;

    private final IThirdAccountService thirdAccountService;

    /**
     * hutool生成图片登录验证码
     * @return
     */
    @ApiOperation("生成图片登录验证码接口")
    @GetMapping
    public R<String> getImageCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        log.info("/login get -> getImageCode; hutool生成图片登录验证码");
        return R.ok(userService.getImageCode(httpServletRequest, httpServletResponse));
    }

    @ApiOperation("用户登录接口")
    @PostMapping
    public R<UserVO> login(@RequestBody @Validated LoginFormDTO loginFormDTO, HttpServletRequest httpServletRequest){
        log.info("/login post -> login: loginFormDTO = {}; 用户登录", loginFormDTO.toString());
        UserVO userVO = userService.login(loginFormDTO, httpServletRequest);
        return R.ok(userVO);
    }

    /**
     * 第三方用户登录
     * @param thirdAccountDTO 第三方登录用户信息体DTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/thirdAccountLogin")
    public R<UserVO> thirdAccountLogin(@RequestBody ThirdAccountDTO thirdAccountDTO) throws Exception {
        log.info("/login/thirdAccountLogin post -> thirdAccountLogin: thirdAccountDTO = {}; 第三方登录用户信息体DTO", thirdAccountDTO.toString());
        UserVO userVO = thirdAccountService.thirdAccountLogin(thirdAccountDTO);
        return R.ok(userVO);
    }


}

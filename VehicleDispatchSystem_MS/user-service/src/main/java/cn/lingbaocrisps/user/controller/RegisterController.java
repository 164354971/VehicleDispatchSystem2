package cn.lingbaocrisps.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.lingbaocrisps.common.domain.R;
import cn.lingbaocrisps.user.domain.dto.RegisterFormDTO;
import cn.lingbaocrisps.user.domain.vo.UserVO;
import cn.lingbaocrisps.user.service.IUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "注册相关接口")
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor  //必备参数变量初始化
@Slf4j
public class RegisterController {

    private final IUserService userService;

    @ApiOperation("用户注册接口")
    @PostMapping
    public R<UserVO> register(@RequestBody @Validated RegisterFormDTO registerFormDTO){
        log.info("/register post -> login: registerFormDTO = {}; 用户注册", registerFormDTO.toString());
        UserVO userVO = userService.register(registerFormDTO);
        return R.ok(userVO);
    }

    @ApiOperation("发送手机验证码")
    @GetMapping("/{phone}")
    public R<String> sendPhoneCode(@PathVariable String phone){
        log.info("/register/send/{phone} get -> sendPhoneCode: phone = {}; 发送手机验证码", phone);
        userService.getPhoneCode(phone);
        return R.ok("验证码已发送");
    }
}

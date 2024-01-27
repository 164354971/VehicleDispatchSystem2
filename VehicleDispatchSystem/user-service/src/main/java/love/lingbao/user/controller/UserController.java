package love.lingbao.user.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.lingbao.common.domain.R;
import love.lingbao.user.domain.dto.LoginFormDTO;
import love.lingbao.user.domain.dto.RegisterFormDTO;
import love.lingbao.user.domain.dto.UserDTO;
import love.lingbao.user.domain.po.User;
import love.lingbao.user.domain.vo.UserVO;
import love.lingbao.user.service.IUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(tags = "用户相关接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor  //必备参数变量初始化
@Slf4j
public class UserController {

    private final IUserService userService;

    @PostMapping("/text")
    public R<String> text(@RequestBody UserDTO userDTO){
        return R.ok("123");
    }

    @GetMapping
    public R<User> getUser(@PathVariable Integer userId){
        return R.ok(userService.getUser(userId));
    }

    @GetMapping("/status/{userId}")
    public R<Integer> getUserStatus(@PathVariable Long userId){
        return R.ok(userService.getUserStatus(userId));
    }

    @GetMapping("/vip/{userId}")
    Integer getUserVipLevel(@PathVariable Integer userId){
        return userService.getUser(userId).getVipLevel();
    }
}

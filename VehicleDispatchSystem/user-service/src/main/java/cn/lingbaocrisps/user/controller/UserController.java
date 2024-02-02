package cn.lingbaocrisps.user.controller;

import cn.lingbaocrisps.common.utils.UserContext;
import cn.lingbaocrisps.user.domain.dto.UserDTO;
import cn.lingbaocrisps.user.domain.po.User;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.lingbaocrisps.common.domain.R;
import cn.lingbaocrisps.user.service.IUserService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{userId}")
    public R<User> getUser(@PathVariable Integer userId){
        return R.ok(userService.getUser(userId));
    }

    @GetMapping("/status/{userId}")
    public R<Integer> getUserStatus(@PathVariable Integer userId){
        return R.ok(userService.getUserStatus(userId));
    }

    @GetMapping("/vip/{userId}")
    Integer getUserVipLevel(@PathVariable Integer userId){
        return userService.getUser(userId).getVipLevel();
    }

    @GetMapping("/img/{userId}")
    public String getUserImg(@PathVariable Integer userId){
        return userService.getUserImg(userId);
    }

    @GetMapping("/logout/{id}")
    public R<String> userLogout(@PathVariable Integer id){
        log.info("/logout/{id} get -> userLogout: id = {}; 用户退出登录", id);
        UserContext.removeUser();
        return R.ok("用户已退出");
    }

    @GetMapping("/userLogin")
    public R<Void> isUserLogin(){
        log.info("/users/userLogin get -> isUserLogin; 用户是否已登录");
        return R.ok();
    }
}

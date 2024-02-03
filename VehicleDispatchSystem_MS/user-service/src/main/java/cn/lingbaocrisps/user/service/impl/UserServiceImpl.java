package cn.lingbaocrisps.user.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.lingbaocrisps.common.exception.BadRequestException;
import cn.lingbaocrisps.common.exception.CommonException;
import cn.lingbaocrisps.common.exception.ForbiddenException;
import cn.lingbaocrisps.common.exception.PhoneException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.lingbaocrisps.common.domain.RedisConstants;
import cn.lingbaocrisps.common.domain.RegexConstants;
import cn.lingbaocrisps.user.config.JwtProperties;
import cn.lingbaocrisps.user.domain.dto.LoginFormDTO;
import cn.lingbaocrisps.user.domain.dto.RegisterFormDTO;
import cn.lingbaocrisps.user.domain.po.User;
import cn.lingbaocrisps.user.domain.vo.UserVO;
import cn.lingbaocrisps.user.enums.UserStatus;
import cn.lingbaocrisps.user.mapper.UserMapper;
import cn.lingbaocrisps.user.service.IUserService;
import cn.lingbaocrisps.user.utils.JwtTool;
import cn.lingbaocrisps.user.utils.RandomCodeUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final PasswordEncoder passwordEncoder;

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    private final RedisTemplate redisTemplate;

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 返回用户信息
     * */
    @Override
    public User getUser(Integer userId) {
        return lambdaQuery().eq(User::getId, userId).one();
    }
    /**
     * 用户登录验证
     * */
    @Override
    public UserVO login(LoginFormDTO loginFormDTO, HttpServletRequest httpServletRequest){
        //1.先验证验证码是否正确
        String loginCode = String.valueOf(httpServletRequest.getSession().getAttribute("loginCode"));
        if(loginFormDTO.getCode() == null || "".equals(loginFormDTO.getCode())){
            throw new CommonException("验证码为空！", 500);
        }
        if(!loginFormDTO.getCode().equals(loginCode)){
            throw new CommonException("验证码输入有误！", 500);
        }
        //2.验证码无误后验证用户是否存在
        String username = loginFormDTO.getUsername();
        String password = loginFormDTO.getPassword();
        User user = lambdaQuery().eq(User::getUsername, username).one();
        if(user == null){
            throw new BadRequestException("用户名输入错误！");
        }
        //3.验证用户密码是否匹配
        if(!isMatePassword(password, user.getPassword())){
            throw new BadRequestException("密码输入错误！");
        }
        //4.判断用户是否被禁用
        if(isUserForbidden(user)){
            throw new ForbiddenException("用户被冻结");
        }
        //5.均匹配成功后生成token
        String token = createToken(user.getId());

        //6.封装vo后返回
        return userToUserVO(user, token);
    }


    /**
     * 通过用户id生成token
     * */
    public String createToken(Integer userId){
        return jwtTool.createToken(userId, jwtProperties.getTokenTTL());
    }

    /**
     * user + token封装成vo
     * */
    public UserVO userToUserVO(User user, String token){
        UserVO vo = new UserVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        //vo.setSalary(user.getSalary());
        vo.setToken(token);
        vo.setUserImg(user.getImg());
        vo.setNickname(user.getNickname());
        return vo;
    }

    /**
     * 验证用户密码是否匹配
     * */
    public boolean isMatePassword(String enterPassword, String password){
        return passwordEncoder.matches(enterPassword, password);
    }

    /**
     * 验证用户是否被禁用
     * 返回true为禁用，返回false为启用
     * */
    public boolean isUserForbidden(User user){
        return user.getStatus() == UserStatus.FROZEN;
    }

    /**
     * 生成前端验证码
     * */
    @Override
    public String getImageCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        //验证码图片的长宽，验证码个数，干扰线条数量
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(130,48,4,25);
        httpServletResponse.setHeader("Cache-Control","no-store");
        httpServletResponse.setHeader("Pragma","no-cache");
        httpServletResponse.setDateHeader("Expires",0);
        httpServletResponse.setContentType("image/gif");

        //生成验证码对象,三个参数分别是宽、高、位数
        SpecCaptcha captcha = new SpecCaptcha(130, 48, 4);
        //设置验证码的字符类型为数字和字母混合
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        // 设置内置字体
        captcha.setCharType(Captcha.FONT_1);
        //设置base64编码
        String s = captcha.toBase64("");
        //验证码存入session
        httpServletRequest.getSession().setAttribute("loginCode",captcha.text().toLowerCase());
        //输出图片流
        //captcha.out(httpServletResponse.getOutputStream());
        return s;
    }

    /**
     * 用户注册
     * */
    @Override
    public UserVO register(RegisterFormDTO registerFormDTO) {
        String phone = registerFormDTO.getPhone();
        String code = registerFormDTO.getCode();
        //1.先判断手机号是否合法
        isPhoneTrue(phone);
        //2.判断手机号是否存在或是否过期
        Long expireTime = stringRedisTemplate.opsForValue().getOperations().getExpire(phone);
        assert expireTime != null;
        if(expireTime.equals(RedisConstants.NO_KEY)){
            throw new PhoneException("验证码已过期");
        }
        //3.验证手机号验证码是否匹配
        String exitCode = stringRedisTemplate.opsForValue().get(phone);
        if(!code.equals(exitCode)){
            throw new PhoneException("验证码输入错误");
        }

        //4.账号判定
        String username = registerFormDTO.getUsername();
        if(!RegexConstants.isMatch(RegexConstants.USERNAME, username)){
            throw new BadRequestException("用户名必须为长度6-16，由大小写字母数字和下划线组成的字符串");
        }

        //5.密码判定
        String password = registerFormDTO.getPassword();
        if(!RegexConstants.isMatch(RegexConstants.PASSWORD, password)){
            throw new BadRequestException("用户密码必须为长度6-16，由大小写字母数字和下划线组成的字符串");
        }

        //6.判定用户账号是否已注册
        User user = lambdaQuery().eq(User::getUsername, username).one();
        if(user != null){
            throw new BadRequestException("用户名已存在");
        }

        //7.判定用户昵称是否已存在
        String nickname = registerFormDTO.getNickname();
        user = lambdaQuery().eq(User::getNickname, nickname).one();
        if(user != null){
            throw new BadRequestException("用户昵称已存在");
        }

        //8.加密密码
        String encodePassword = passwordEncoder.encode(password);

        //9.创建user，存入数据库
        user = new User();
        user.setUsername(username);
        user.setPassword(encodePassword);
        user.setPhone(phone);
        user.setNickname(nickname);
        save(user);

        //10.存入数据库后再取出（获得用户id）
        user = lambdaQuery().eq(User::getUsername, username).one();
        String token = createToken(user.getId());
        UserVO userVO = userToUserVO(user, token);
        return userVO;
    }

    /**
     * 生成手机号验证码
     * */
    @Override
    public void getPhoneCode(String phone) {
        //手机号校验
        isPhoneTrue(phone);

        //判断是否已注册
        User user = lambdaQuery().eq(User::getPhone, phone).one();
        if(user != null){
            throw new PhoneException("手机号已注册");
        }
        //生成六位数验证码
        String code = RandomCodeUtils.getSixValidationCode();

        /*//腾讯云短信服务已过期
        *********************************
        //短信格式插入字符串数组，第一个是验证码，第二个是时长（分钟）
        String[] params = {code, "3"};
        //发送短信，获取发送的结果
        SmsSingleSenderResult result = null;
        try {
            result = SendMsgUtils.sendMsgByTxPlatform(phone, params);
        } catch (Exception e) {
            throw new PhoneException(e.getMessage());
        }
        //结果的结果码为1016，表示手机号的格式输入有误
        if(result.result == 1016) {
            throw new PhoneException("手机号格式输入有误！请重新输入");
        }
        //其他错误，抛异常
        else if(result.result != 0){
            throw new PhoneException("send phone validateCode is error");
        }*/
        //将手机号与验证码存入redis,一分钟过期时间
        redisTemplate.opsForValue().set(phone, code, 3, TimeUnit.MINUTES);
        log.info("phone = {}, code = {}", phone, code);
    }

    /**
     * 手机号合法校验
     * */
    private void isPhoneTrue(String phone){
        //位数判断
        if(phone.length() != 11){
            throw new PhoneException("手机号位数错误");
        }
        //合法校验
        try{
            Long phoneNumber = Long.getLong(phone);
        }catch (NumberFormatException e){
            throw new PhoneException("手机号非纯数字");
        }
    }

    /**
     * 返回用户状态
     * */
    public Integer getUserStatus(Integer userId){
        QueryWrapper<User> qw = new QueryWrapper<>();
        User user = getOne(qw.eq("id", userId));
        return user.getStatus().getValue();
    }

    @Override
    public String getUserImg(Integer userId) {
        return this.baseMapper.getUserImg(userId);
    }


}

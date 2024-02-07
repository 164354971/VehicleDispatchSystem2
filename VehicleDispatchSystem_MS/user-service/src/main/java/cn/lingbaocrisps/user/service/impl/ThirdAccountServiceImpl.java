package cn.lingbaocrisps.user.service.impl;

import cn.lingbaocrisps.user.domain.bo.UserImgBO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import cn.lingbaocrisps.common.exception.CommonException;
import cn.lingbaocrisps.user.config.JwtProperties;
import cn.lingbaocrisps.user.domain.dto.ThirdAccountDTO;
import cn.lingbaocrisps.user.domain.po.ThirdAccount;
import cn.lingbaocrisps.user.domain.po.User;
import cn.lingbaocrisps.user.domain.vo.UserVO;
import cn.lingbaocrisps.user.mapper.ThirdAccountMapper;
import cn.lingbaocrisps.user.service.IThirdAccountService;
import cn.lingbaocrisps.user.service.IUserService;
import cn.lingbaocrisps.user.utils.JwtTool;
import org.apache.commons.io.FileUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ThirdAccountServiceImpl extends ServiceImpl<ThirdAccountMapper, ThirdAccount> implements IThirdAccountService {

    private final IUserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtTool jwtTool;

    private final JwtProperties jwtProperties;

    private final RabbitTemplate rabbitTemplate;


    @Override
    public UserVO thirdAccountLogin(ThirdAccountDTO thirdAccountDTO) {
        //1 先查找第三方用户是否有绑定账户
        LambdaQueryWrapper<ThirdAccount> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ThirdAccount::getThirdUniqueAccount, thirdAccountDTO.getThirdUniqueAccount());
        ThirdAccount thirdAccount = getOne(lqw);

        //2 如果存在则代表第三方已绑定用户，无需再创建新用户
        if(thirdAccount != null){
            User user = userService.getById(thirdAccount.getUserId());
            if(user == null)
                throw new CommonException("第三方登录失败，请联系管理员", 411);
            //构建token,将用户vo返回
            return userToUserVO(user, createToken(user.getId()));
        }

        //3 如果没有查找到绑定用户，则先查找当前是否有登录用户，若有则添加绑定，无则新建用户，再绑定

        /*//3.1 判断当前是否有登录用户
        Object objId = httpSession.getAttribute("user");

        if(objId != null){
            //3.1.1 如果当前有登录用户，则直接绑定
            BigInteger id = (BigInteger) objId;
            //3.1.2 获取登录用户
            User user = userService.getById(id);
            UserVo userVo = new UserVo(user);
            //3.1.3 保存用户ID到第三方
            thirdAccount.setUserId(id);
            thirdAccountService.save(thirdAccount);

            return R.success(userVo, "登录成功！");
        }*/
        //3.2 生成新用户

        //3.2.1 随机用户名(10位）同时做昵称
        //String randomUsername = UUID.randomUUID().toString().replaceAll("-", "");
        String randomUsername = randomString(10);
        //3.2.2 随机密码(16位)
        String randomPassword = randomString(16);

        /*//3.2.3 md5加密
        randomPassword = DigestUtils.md5DigestAsHex(randomPassword.getBytes());*/
        //3.2.3.加密密码(默认a123456)
        String encodePassword = passwordEncoder.encode("a123456");

        //3.2.4 保存用户
        User user = new User();
        user.setUsername(randomUsername);
        user.setNickname(thirdAccountDTO.getNickname());
        user.setPassword(encodePassword);
        user.setGender(thirdAccountDTO.getGender());
        user.setImg(thirdAccountDTO.getImg());
        userService.save(user);

        //3.2.5 获取用户ID
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername, randomUsername);
        user = userService.getOne(userLambdaQueryWrapper);

        //3.2.6 保存第三方用户信息
        Integer userId = user.getId();

        UserImgBO userImgBO = new UserImgBO();
        userImgBO.setUserId(userId);
        userImgBO.setImgUrl(thirdAccountDTO.getImg());
        userImgBO.setUsername(randomUsername);

        //消息队列下载微信头像图片到本地服务器
        rabbitTemplate.convertAndSend("user.topic", "user.success", userImgBO);

        thirdAccount = new ThirdAccount();
        thirdAccount.setUserId(userId);
        thirdAccount.setThirdUniqueAccount(thirdAccountDTO.getThirdUniqueAccount());
        thirdAccount.setType(thirdAccountDTO.getType());
        thirdAccount.setBindFlag(true);

        save(thirdAccount);

        //3.2.7 返回用户vo信息
        return userToUserVO(user, createToken(userId));
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
        vo.setNickname(user.getNickname());
        vo.setUserImg(user.getImg());
        //vo.setSalary(user.getSalary());
        vo.setToken(token);
        vo.setVipLevel(user.getVipLevel());
        return vo;
    }

    public String randomString(int randomNum){
        Random random = new Random();
        String alphabetsInUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String alphabetsInLowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String allCharacters = alphabetsInLowerCase + alphabetsInUpperCase + numbers;
        StringBuilder randomBuilder = new StringBuilder();
        for (int i = 0; i < randomNum; i++) {
            int randomIndex = random.nextInt(allCharacters.length());
            randomBuilder.append(allCharacters.charAt(randomIndex));
        }
        return randomBuilder.toString();
    }


}

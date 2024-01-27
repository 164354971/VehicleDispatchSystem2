package love.lingbao.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThirdAccountDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String thirdUniqueAccount;//'第三方唯一用户id，可以是微信的openid，可以是QQ的openid，抑或苹果id',
    private Integer type;//tinyint    default 0  '标识第三方类型，这里规定1.代表微信，2.代表QQ，3.代表苹果',
    private String nickname;//第三方网名
    private String img;//头像
    private String gender;//性别
}

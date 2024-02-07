package cn.lingbaocrisps.user.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtherVO {
    private Integer userId;
    private String username;
    private String nickname;
    private String userImg;
    private Integer vipLevel;
}

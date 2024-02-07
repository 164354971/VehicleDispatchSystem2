package cn.lingbaocrisps.api.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBO {
    private Integer userId;
    private String username;
    private String nickname;
    private String userImg;
    private Integer vipLevel;
}

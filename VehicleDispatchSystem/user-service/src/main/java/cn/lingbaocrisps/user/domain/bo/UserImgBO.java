package cn.lingbaocrisps.user.domain.bo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserImgBO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer userId;
    private String username;
    private String imgUrl;
}

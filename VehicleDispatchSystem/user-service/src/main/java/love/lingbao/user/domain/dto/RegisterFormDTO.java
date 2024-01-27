package love.lingbao.user.domain.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "注册表单实体")
public class RegisterFormDTO {
    @ApiModelProperty(value = "用户名", required = true)
    @NotNull(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotNull(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "昵称", required = true)
    @NotNull(message = "用户昵称不能为空")
    private String nickname;

    @ApiModelProperty(value = "注册手机号", required = true)
    @NotNull(message = "用户注册手机号不能为空")
    private String phone;

    @ApiModelProperty(value = "客户端验证码", required = true)
    @NotNull(message = "注册验证码不能为空")
    private String code;
}

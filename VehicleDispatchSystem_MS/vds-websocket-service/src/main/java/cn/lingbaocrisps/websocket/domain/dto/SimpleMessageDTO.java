package cn.lingbaocrisps.websocket.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SimpleMessageDTO implements Serializable {
    private final static Long serialVersionUID = 1L;
    private Integer userId;
    private String username;
    private String nickname;
    private String userImg;
    private String lastContent;
    private Long lastContentTimestamp;
    private Integer noticeContentNum;
}

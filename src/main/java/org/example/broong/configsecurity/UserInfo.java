package org.example.broong.configsecurity;

import java.util.List;
import lombok.Data;

@Data
public class UserInfo {
    private Long id;
    private String password;
    private String email;

    private List<UserInfoAuth> securityAuthList;

}

package cn.fuzhizhuang.middleware.govern.test.interfaces;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fuzhizhuang
 * @description user information
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private String code;

    private String info;

    private String name;

    private Integer age;

    private String address;
}

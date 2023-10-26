package cn.fuzhizhuang.middleware.methodintercept.test.interfaces;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fuzhizhuang
 * @description user information
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {

    private String code;

    private String info;

    private String name;

    private Integer age;

    private String address;
}

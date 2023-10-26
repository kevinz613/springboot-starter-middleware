package cn.fuzhizhuang.middleware.methodintercept.test.interfaces;

import cn.fuzhizhuang.middleware.methodintercept.annotation.UseMethodIntercept;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author fuzhizhuang
 * @description api
 */

@Controller
@ResponseBody
@RequestMapping("/api")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @UseMethodIntercept(method = "blacklist",returnJson = "{\"code\":\"1111\",\"info\":\"自定义拦截方法，不允许访问!\"}")
    @RequestMapping(value = "/queryUserInfo",method = RequestMethod.GET)
    public UserInfo queryUserInfo(@RequestParam String userId) throws InterruptedException {
        logger.info("查询用户信息,userId:{}",userId);
        UserInfo userInfo = new UserInfo();
        userInfo.setCode("0000");
        userInfo.setInfo("success");
        userInfo.setName("白白:"+userId);
        userInfo.setAge(24);
        userInfo.setAddress("上海市普陀区华东师范大学中北校区");
        return userInfo;
    }
    public boolean blacklist(@RequestParam String userId){
        if ("bbb".equals(userId)|| "222".equals(userId)){
            logger.info("拦截自定义黑名单用户-userId:{}",userId);
            return false;
        }
        return true;
    }
}

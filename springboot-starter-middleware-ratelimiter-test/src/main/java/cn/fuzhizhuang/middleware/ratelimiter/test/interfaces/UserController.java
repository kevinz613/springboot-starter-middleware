package cn.fuzhizhuang.middleware.ratelimiter.test.interfaces;

import cn.fuzhizhuang.middleware.ratelimiter.annotation.UseRateLimiter;
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

    @UseRateLimiter(permitsPerSecond = 1, returnJson = "{\"code\":\"1111\",\"info\":\"调用方法超过最大次数,限流返回！\"}")
    @RequestMapping(value = "/queryUserInfo", method = RequestMethod.GET)
    public UserInfo queryUserInfo(@RequestParam String userId) throws InterruptedException {
        logger.info("查询用户信息,userId:{}", userId);
        UserInfo userInfo = new UserInfo();
        userInfo.setCode("0000");
        userInfo.setInfo("success");
        userInfo.setName("白白:" + userId);
        userInfo.setAge(24);
        userInfo.setAddress("上海市普陀区华东师范大学中北校区");
        return userInfo;
    }
}

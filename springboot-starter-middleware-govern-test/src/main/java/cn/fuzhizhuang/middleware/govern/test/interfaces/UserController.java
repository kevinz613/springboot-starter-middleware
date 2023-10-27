package cn.fuzhizhuang.middleware.govern.test.interfaces;

import cn.fuzhizhuang.middleware.govern.annotation.UseHystrix;
import cn.fuzhizhuang.middleware.govern.annotation.UseMethodIntercept;
import cn.fuzhizhuang.middleware.govern.annotation.UseRateLimiter;
import cn.fuzhizhuang.middleware.govern.annotation.UseWhiteList;
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


    @UseWhiteList(key = "userId", returnJson = "{\"code\":\"1111\",\"info\":\"非白名单可访问用户拦截！\"}")
    @UseRateLimiter(permitsPerSecond = 1, returnJson = "{\"code\":\"1111\",\"info\":\"调用方法超过最大次数,限流返回！\"}")
    @UseMethodIntercept(method = "blacklist", returnJson = "{\"code\":\"1111\",\"info\":\"自定义拦截方法，不允许访问!\"}")
    @UseHystrix(returnJson = "{\"code\":\"1111\",\"info\":\"调用方法超过400ms,熔断返回！\"}")
    @RequestMapping(value = "/queryUserInfo", method = RequestMethod.GET)
    public UserInfo queryUserInfo(@RequestParam String userId) throws InterruptedException {
        logger.info("查询用户信息,userId:{}", userId);
        Thread.sleep(500);
        UserInfo userInfo = new UserInfo();
        userInfo.setCode("0000");
        userInfo.setInfo("success");
        userInfo.setName("白白:" + userId);
        userInfo.setAge(24);
        userInfo.setAddress("上海市普陀区华东师范大学中北校区");
        return userInfo;
    }

    public boolean blacklist(@RequestParam String userId) {
        if ("bbb".equals(userId) || "222".equals(userId)) {
            logger.info("拦截自定义黑名单用户-userId:{}", userId);
            return false;
        }
        return true;
    }
}

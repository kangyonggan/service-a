package com.kangyonggan.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kyg
 */
@SpringBootApplication
@RestController("/")
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    /**
     * 必须要使用域名，不能使用localhost或者127.0.0.1，否则调用的就是容器内部的服务了
     */
    private static final String SERVICE_B = "http://dev.kangyonggan.com/service-b";
    private static final String SERVICE_C = "http://dev.kangyonggan.com/service-c";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功会返回用户信息
     * @throws Exception 抛异常登录失败
     */
    @PostMapping("login")
    public Map<String, Object> login(@RequestParam String username, @RequestParam String password) throws Exception {
        Map<String, Object> resultMap = new HashMap<>(4);
        log.info("用户登录：{}:{}", username, password);

        // 请求服务B的检测用户信息接口
        Map<String, String> checkUserParams = new HashMap<>(4);
        checkUserParams.put("username", username);
        checkUserParams.put("password", password);
        String checkResult = HttpUtil.post(SERVICE_B + "/checkUser", checkUserParams);
        if (!"ok".equals(checkResult)) {
            resultMap.put("success", false);
            resultMap.put("msg", "用户名或密码错误");
            return resultMap;
        }

        // 请求服务C获取用户信息
        String userInfo = HttpUtil.get(SERVICE_C + "/userInfo?username=" + username);
        resultMap.put("success", true);
        resultMap.put("userInfo", userInfo);
        return resultMap;
    }

    /**
     * 用户账单查询（模拟较大响应体）
     *
     * @param username 用户名
     * @return 返回用户的本月账单
     * @throws Exception 抛异常查询失败
     */
    @GetMapping("queryOrders")
    public Map<String, Object> queryOrders(@RequestParam String username) throws Exception {
        Map<String, Object> resultMap = new HashMap<>(4);
        log.info("用户账单查询：{}", username);

        // 请求服务C获取用户信息
        String orders = HttpUtil.get(SERVICE_C + "/queryOrders?username=" + username);
        resultMap.put("success", true);
        resultMap.put("orders", orders);
        return resultMap;
    }
}

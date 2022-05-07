package xh.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiehuang
 * @date 2022/05/06 3:10
 */
@RestController
@RequestMapping("hello")
public class HelloController {
    @RequestMapping("say")
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}

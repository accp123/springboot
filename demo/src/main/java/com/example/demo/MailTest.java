package com.example.demo;

import com.example.demo.service.impl.JavaMailComponent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTest {
    @Autowired
    private JavaMailComponent javaMailComponent;

    @Test
    public void test() {
        this.javaMailComponent.sendMail("329386467@qq.com");
    }
}

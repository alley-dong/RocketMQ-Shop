package com.mashibing;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**

 *类说明：springboot的启动类
 */
@SpringBootApplication
@MapperScan("com.mashibing.dao")

public class ShopUserApplication {
    //启动方法(相当于启动一个Tomcat,同时默认端口8082)
    public static void main(String[] args) {
        SpringApplication.run(ShopUserApplication.class, args);
    }
}
package com.pears.asa.config.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode

@Component
//在springboot升级到2.x.x以后，不允许configuration的property用驼峰的命名方式，要用‘-’分割
@ConfigurationProperties(prefix="email-config")
@PropertySource("classpath:email.properties")
public class EmailConfig {
    private String host;
    private String username;
    private String password;
    private String urlhost;

}







package com.cn.huobi.config.webconfig;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 开发者 liaoliping
 * date：2017/12/29
 * time：23:08
 */
@Component
public class CustomServletContainer implements EmbeddedServletContainerCustomizer {
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8081);
        container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/404.html"));
        container.setSessionTimeout(10, TimeUnit.MINUTES);
    }
}

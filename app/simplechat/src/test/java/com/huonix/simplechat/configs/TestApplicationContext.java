package com.huonix.simplechat.configs;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class TestApplicationContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(final ApplicationContext context) throws BeansException {
        TestApplicationContext.context = context;
    }

    public static Object getBean(final String beanName) {
        return context.getBean(beanName);
    }

}
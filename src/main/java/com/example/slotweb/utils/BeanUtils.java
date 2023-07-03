package com.example.slotweb.utils;

import com.example.slotweb.extend.ApplicationContextProvider;
import org.springframework.beans.BeansException;

public class BeanUtils {
	
    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return ApplicationContextProvider.getApplicationContext().getBean(clazz);
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T getBean(String name) throws BeansException {
		return (T)ApplicationContextProvider.getApplicationContext().getBean(name);
	}
    
	public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		return ApplicationContextProvider.getApplicationContext().getBean(name, requiredType);
	}
    
}



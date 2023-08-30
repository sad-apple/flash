package com.example.smartdoc.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author zsp
 * @date 2023/8/29 14:33
 */
@Component
public class StudentServiceFactoryBean implements FactoryBean<StudentService> {

    private String type;

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public StudentService getObject() {
//        if (type.equals("hs")) {
//            return new HsStudentService();
//        }
//        if (type.equals("ls")) {
//            return new LsStudentService();
//        }
        return new LsStudentService();
    }

    @Override
    public Class<?> getObjectType() {
        return StudentService.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

}

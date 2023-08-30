package com.example.event.controller;

import com.example.event.entity.Student;
import com.example.event.pusher.DemoPusher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 事件管理
 * @author zsp
 * @date 2023/8/29 15:19
 */
@RequestMapping("/event")
@RestController
public class EventController {

    @Autowired
    private DemoPusher demoPusher;

    @GetMapping("/success")
    @Transactional()
    public void success(String name) {
        Student student = new Student();
        student.setRealname(name);
        student.setAge(12);
        student.setIdCard("341181199110054813");
        student.insert();
//        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        demoPusher.success(name);
        System.out.println("事务提交了吗");
    }

}

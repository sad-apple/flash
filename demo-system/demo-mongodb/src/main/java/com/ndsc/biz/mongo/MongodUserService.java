package com.ndsc.biz.mongo;

import com.ndsc.biz.mongo.entity.User;
import com.ndsc.biz.mongo.repository.UserRepository;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

/**
 * @author zsp
 * @date 2023/4/12 11:22
 */
@Service
public class MongodUserService {

    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public Page<User> page(Integer page, Integer size) {
        Page<User> all = userRepository.findAll(PageRequest.of(page, size));
        return all;
    }

    public boolean removeById(String id) {
        userRepository.deleteAllById(Collections.singleton(id));
        return true;
    }

    public boolean updateById(User user) {
        userRepository.save(user);
        return true;
    }

    public User selectById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public void uploadAvatar(String id, MultipartFile file) throws IOException {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            user.setAvatar(new Binary(file.getBytes()));
            userRepository.save(user);
        }
    }

}

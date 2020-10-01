package com.maksym.task.service;

import com.maksym.task.exception.UserNotFoundException;
import com.maksym.task.model.User;
import com.maksym.task.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user, long userId) {
        User oldUser = findById(userId);
        modelMapper.map(user, oldUser);
        oldUser.setId(userId);
        return userRepository.save(oldUser);
    }
}

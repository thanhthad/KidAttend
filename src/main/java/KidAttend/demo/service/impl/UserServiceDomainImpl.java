package KidAttend.demo.service.impl;


import KidAttend.demo.entity.User;
import KidAttend.demo.exception.user.UserNotFoundException;
import KidAttend.demo.repository.UserRepository;
import KidAttend.demo.service.UserServiceDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class UserServiceDomainImpl  implements UserServiceDomain {
    private final UserRepository userRepository;

    @Override
    public void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public User getByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User not exists with id: "+ userId)
        );
    }
}
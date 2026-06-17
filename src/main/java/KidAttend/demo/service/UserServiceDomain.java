package KidAttend.demo.service;


import KidAttend.demo.entity.User;

public interface UserServiceDomain {

    public void validateUserExists(Long userId);

    public User getByUserId(Long userId);
}
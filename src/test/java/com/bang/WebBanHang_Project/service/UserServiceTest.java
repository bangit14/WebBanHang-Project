package com.bang.WebBanHang_Project.service;

import com.bang.WebBanHang_Project.common.Gender;
import com.bang.WebBanHang_Project.common.UserStatus;
import com.bang.WebBanHang_Project.common.UserType;
import com.bang.WebBanHang_Project.model.UserEntity;
import com.bang.WebBanHang_Project.repository.AddressRepository;
import com.bang.WebBanHang_Project.repository.UserRepository;
import com.bang.WebBanHang_Project.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    private @Mock UserRepository userRepository;
    private @Mock AddressRepository addressRepository;
    private @Mock PasswordEncoder passwordEncoder;
    private @Mock EmailService emailService;

    private static UserEntity tayJava;
    private static UserEntity johnDoe;


    @BeforeAll
    static void beforeAll(){

        tayJava = new UserEntity();
        tayJava.setId(1L);
        tayJava.setFirstName("Tay");
        tayJava.setLastName("Java");
        tayJava.setGender(Gender.MALE);
        tayJava.setBirthday(new Date());
        tayJava.setEmail("quoctay87@gmail.com");
        tayJava.setPhone("0975118228");
        tayJava.setUsername("tayjava");
        tayJava.setPassword("password");
        tayJava.setType(UserType.USER);
        tayJava.setStatus(UserStatus.ACTIVE);

        johnDoe = new UserEntity();
        johnDoe.setId(2L);
        johnDoe.setFirstName("John");
        johnDoe.setLastName("Doe");
        johnDoe.setGender(Gender.FEMALE);
        johnDoe.setBirthday(new Date());
        johnDoe.setEmail("johndoe@gmail.com");
        johnDoe.setPhone("0123456789");
        johnDoe.setUsername("johndoe");
        johnDoe.setPassword("password");
        johnDoe.setType(UserType.USER);
        johnDoe.setStatus(UserStatus.INACTIVE);
    }

    @BeforeEach
    void setUp() {

        userService = new UserServiceImpl(userRepository,addressRepository,passwordEncoder,emailService);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByUsername() {
    }

    @Test
    void findByEmail() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void delete() {
    }
}
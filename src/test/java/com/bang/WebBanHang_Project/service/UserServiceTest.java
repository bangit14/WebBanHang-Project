package com.bang.WebBanHang_Project.service;

import com.bang.WebBanHang_Project.common.Gender;
import com.bang.WebBanHang_Project.common.UserStatus;
import com.bang.WebBanHang_Project.common.UserType;
import com.bang.WebBanHang_Project.controller.request.AddressRequest;
import com.bang.WebBanHang_Project.controller.request.UserCreationRequest;
import com.bang.WebBanHang_Project.controller.request.UserPasswordRequest;
import com.bang.WebBanHang_Project.controller.request.UserUpdateRequest;
import com.bang.WebBanHang_Project.controller.response.UserPageResponse;
import com.bang.WebBanHang_Project.controller.response.UserResponse;
import com.bang.WebBanHang_Project.exception.ResourceNotFoundException;
import com.bang.WebBanHang_Project.model.UserEntity;
import com.bang.WebBanHang_Project.repository.AddressRepository;
import com.bang.WebBanHang_Project.repository.UserRepository;
import com.bang.WebBanHang_Project.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    private @Mock UserRepository userRepository;
    private @Mock AddressRepository addressRepository;
    private @Mock PasswordEncoder passwordEncoder;
    private @Mock EmailService emailService;

    private static UserEntity bangIt;
    private static UserEntity johnDoe;


    @BeforeAll
    static void beforeAll(){
// Dữ liệu giả lập
        bangIt = new UserEntity();
        bangIt.setId(1L);
        bangIt.setFirstName("Tay");
        bangIt.setLastName("Java");
        bangIt.setGender(Gender.MALE);
        bangIt.setBirthday(new Date());
        bangIt.setEmail("quoctay87@gmail.com");
        bangIt.setPhone("0975118228");
        bangIt.setUsername("bangIt");
        bangIt.setPassword("password");
        bangIt.setType(UserType.USER);
        bangIt.setStatus(UserStatus.ACTIVE);

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
        // Khởi tạo lớp triển khai của UserService
        userService = new UserServiceImpl(userRepository, addressRepository, passwordEncoder, emailService);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetListUsers_success() {
// Giả lập phương thức search của UserRepository
        Page<UserEntity> userPage = new PageImpl<>(List.of(bangIt, johnDoe));
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        // Gọi phương thức cần kiểm tra
        UserPageResponse result = userService.findAll(null, null, 0, 20);

        Assertions.assertNotNull(result);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testSearchUser_success() {
// Giả lập phương thức search của UserRepository
        Page<UserEntity> userPage = new PageImpl<>(List.of(bangIt, johnDoe));
        when(userRepository.searchByKeyword(any(), any(Pageable.class))).thenReturn(userPage);

        // Gọi phương thức cần kiểm tra
        UserPageResponse result = userService.findAll("tay", null, 0, 20);

        Assertions.assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("bangIt", result.getUsers().get(0).getUsername());
    }

    @Test
    void testGetUserList_Empty() {
        // Giả lập hành vi của UserRepository
        Page<UserEntity> userPage = new PageImpl<>(List.of());
        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        // Gọi phương thức cần kiểm tra
        UserPageResponse result = userService.findAll(null, null, 0, 20);

        Assertions.assertNotNull(result);
        assertEquals(0, result.getUsers().size());
    }

    @Test
    void testGetUserById_Success() {
        // Giả lập hành vi của UserRepository
        when(userRepository.findById(1l)).thenReturn(Optional.of(bangIt));

        // Gọi phương thức cần kiểm tra
        UserResponse result = userService.findById(1l);

        Assertions.assertNotNull(result);
        assertEquals("bangIt",result.getUsername());
    }

    @Test
    void testGetUserById_Failure(){
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class,() -> userService.findById(10l));
        assertEquals("User not found", thrown.getMessage());
    }

    @Test
    void testFindByUsername_Success(){
        // Giả lập hành vi của UserRepository
        when(userRepository.findByUsername("bangIt")).thenReturn(bangIt);

        // Gọi phương thức cần kiểm tra
        UserResponse result = userService.findByUsername("bangIt");

        Assertions.assertNotNull(result);
        assertEquals("bangIt",result.getUsername());
    }
    
    @Test
    void testFindByEmail_success(){
        // Giả lập hành vi của UserRepository
        when(userRepository.findByEmail("candyyeukoi10@gmail.com")).thenReturn(bangIt);

        // Gọi phương thức cần kiểm tra
        UserResponse result = userService.findByEmail("candyyeukoi10@gmail.com");

        Assertions.assertNotNull(result);
        assertEquals("candyyeukoi10@gmail.com",result.getEmail());
    }

    @Test
    void testSave_Success(){
        // Giả lập hành vi của UserRepository
        when(userRepository.save(any(UserEntity.class))).thenReturn(bangIt);

        UserCreationRequest userCreationRequest = new UserCreationRequest();
        userCreationRequest.setFirstName("Bang");
        userCreationRequest.setLastName("IT");
        userCreationRequest.setGender(Gender.MALE);
        userCreationRequest.setBirthday(new Date());
        userCreationRequest.setEmail("candyyeukoi10@gmail.com");
        userCreationRequest.setPhone("0974684656");
        userCreationRequest.setUsername("bangit");

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setApartmentNumber("apartment");
        addressRequest.setFloor("floor");
        addressRequest.setBuilding("building");
        addressRequest.setStreetNumber("streetnumber");
        addressRequest.setCity("city");
        addressRequest.setCountry("country");
        addressRequest.setAddressType(1);
        userCreationRequest.setAddresses(List.of(addressRequest));

        // Gọi phương thức kiểm tra
        long result = userService.save(userCreationRequest);

        // kiểm tra kết quả
        assertNotNull(result);
        assertEquals(1l,result);
    }

    @Test
    void testUpdate_Success(){
        Long user_id = 2L;

        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setId(user_id);
        updateRequest.setFirstName("Jane");
        updateRequest.setLastName("Smith");
        updateRequest.setGender(Gender.FEMALE);
        updateRequest.setBirthday(new Date());
        updateRequest.setEmail("janesmith@gmail.com");
        updateRequest.setPhone("0123456789");
        updateRequest.setUsername("janesmith");

        AddressRequest addressRequest = new AddressRequest();
        addressRequest.setApartmentNumber("apartmentNumber");
        addressRequest.setApartmentNumber("apartment");
        addressRequest.setFloor("floor");
        addressRequest.setBuilding("building");
        addressRequest.setStreetNumber("streetnumber");
        addressRequest.setCity("city");
        addressRequest.setCountry("country");
        addressRequest.setAddressType(1);
        updateRequest.setAddresses(List.of(addressRequest));

        // Gọi phương thức cần kiểm tra
        userService.update(updateRequest);

        UserResponse result = userService.findById(user_id);

        assertEquals("janesmith",result.getUsername());
        assertEquals("janesmith@gmail.com",result.getEmail());
    }

    @Test
    void testChangePassword_Success(){
        Long userId = 2L;

        UserPasswordRequest request = new UserPasswordRequest();
        request.setId(userId);
        request.setPassword("newPassword");
        request.setConfirmPassword("newPassword");

        // Giả lập hành vi của repository và password encoder
        when(userRepository.findById(userId)).thenReturn(Optional.of(johnDoe));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodeNewPassword");

        // Gọi phương thức cần kiểm tra
        userService.changePassword(request);

        // Kiểm tra mật khẩu được mã hóa và lưu
        assertEquals("encodeNewPassWord", johnDoe.getPassword());
        verify(userRepository,times(1)).save(johnDoe);
        verify(passwordEncoder,times(1)).encode(request.getPassword());
    }

    @Test
    void testDeleteUser_Success(){
        // Chuẩn bị dữ liệu
        Long userId = 1L;

        // Giả lập hành vi repository
        when(userRepository.findById(userId)).thenReturn(Optional.of(bangIt));

        // Gọi phương thức cần kiểm tra
        userService.delete(userId);

        // Kiểm tra kết quả
        assertEquals(UserStatus.INACTIVE,bangIt.getStatus());
        verify(userRepository,times(1)).save(bangIt);
    }

    @Test
    void testUserNotFound_ThrowsException(){
        // Chuẩn bị dữ liệu
        Long userId = 1L;

        // Giả lập hành vi repository
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Gọi phương thức và kiểm tra ngoại lệ
        Exception exception = assertThrows(ResourceNotFoundException.class,() -> userService.delete(userId));

        // Kiểm tra nội dung ngoại lệ
        assertEquals("User not found", exception.getMessage());
        verify(userRepository,never()).save(any(UserEntity.class));
    }

}

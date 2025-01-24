package com.bang.WebBanHang_Project.controller;

import com.bang.WebBanHang_Project.common.Gender;
import com.bang.WebBanHang_Project.controller.response.UserPageResponse;
import com.bang.WebBanHang_Project.controller.response.UserResponse;
import com.bang.WebBanHang_Project.service.JwtService;
import com.bang.WebBanHang_Project.service.UserService;
import com.bang.WebBanHang_Project.service.UserServiceDetail;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserServiceDetail userServiceDetail;

    @MockBean
    private JwtService jwtService;

    private static UserResponse bangIt;
    private static UserResponse johnDoe;

    @BeforeAll
    static void setUp(){

        bangIt = new UserResponse();
        bangIt.setId(1L);
        bangIt.setFirstName("Bang");
        bangIt.setLastName("IT");
        bangIt.setGender(Gender.MALE);
        bangIt.setBirthday(new Date());
        bangIt.setEmail("candyyeukoi10@gmail.com");
        bangIt.setPhone("097468465");
        bangIt.setUsername("bangit");

        johnDoe = new UserResponse();
        johnDoe.setId(2L);
        johnDoe.setFirstName("John");
        johnDoe.setLastName("Doe");
        johnDoe.setGender(Gender.FEMALE);
        johnDoe.setBirthday(new Date());
        johnDoe.setEmail("johndoe@gmail.com");
        johnDoe.setPhone("0123456789");
        johnDoe.setUsername("johndoe");
    }

    @Test
    @WithMockUser(authorities = {"admin","manager"})
    void shouldGetUserList() throws Exception {
        List<UserResponse> userListResponses = List.of(bangIt,johnDoe);

        UserPageResponse userPageResponse = new UserPageResponse();
        userPageResponse.setPageNumber(0);
        userPageResponse.setPageSize(20);
        userPageResponse.setTotalPages(1);
        userPageResponse.setTotalElements(2);
        userPageResponse.setUsers(userListResponses);

        when(userService.findAll(null,null,0,20)).thenReturn(userPageResponse);

        // Perform the test
        mockMvc.perform(get("/user/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", is("user list")))
                .andExpect(jsonPath("$.data.totalPages", is(1)))
                .andExpect(jsonPath("$.data.totalElements", is(2)))
                .andExpect(jsonPath("$.data.users[0].id", is(1)))
                .andExpect(jsonPath("$.data.users[0].username", is("bangit")));
    }

    @Test
    @WithMockUser(authorities = {"admin","manager"})
    void shouldGetUserDetail() throws Exception {
        when(userService.findById(anyLong())).thenReturn(bangIt);

        // Perform the test
        mockMvc.perform(get("/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", is("user")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.firstName", is("Bang")))
                .andExpect(jsonPath("$.data.lastName", is("IT")))
                .andExpect(jsonPath("$.data.email", is("candyyeukoi10@gmail.com")));
    }
}

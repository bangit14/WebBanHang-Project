package com.bang.WebBanHang_Project;

import com.bang.WebBanHang_Project.controller.AuthenticationController;
import com.bang.WebBanHang_Project.controller.EmailController;
import com.bang.WebBanHang_Project.controller.UserController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebBanHangProjectApplicationTests {


	@InjectMocks
	private AuthenticationController authenticationController;

	@InjectMocks
	private UserController userController;

	@InjectMocks
	private EmailController emailController;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(authenticationController);
		Assertions.assertNotNull(emailController);
		Assertions.assertNotNull(userController);
	}

}

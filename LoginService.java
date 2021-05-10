package com.scube.boot.service;

import java.util.List;

import com.scube.boot.bean.LoginBO;
import com.scube.boot.vo.LoginVO;

public interface LoginService {

	LoginBO checkUserDetails(String userName, String password);

	int insertUserDetails(LoginBO loginBO);

	boolean checkSameEmailId(String emailId);

	boolean checkMobileNumber(Long mobileNumber);

	List<LoginBO> viewUserRegDetails();

	List<LoginBO> viewUserRegistrationDetails(int userid);

	int deleteProductDetails(LoginBO loginBO1);

	LoginBO getUserDetailsssss(int userID);

	boolean userStatus(LoginBO loginBO);

	LoginVO postUsernameLogin(String username);



}


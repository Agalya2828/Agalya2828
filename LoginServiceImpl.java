package com.scube.boot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scube.boot.bean.LoginBO;
import com.scube.boot.dao.LoginDao;
import com.scube.boot.vo.LoginVO;

@Service
@Transactional
public class LoginServiceImpl implements LoginService{

	@Autowired
	LoginDao loginDao;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public LoginBO checkUserDetails(String userName, String password) {

		LoginBO loginBO=new LoginBO();

		try{
			LoginVO loginVO=loginDao.checkUserDetails(userName,password);

			if(null!=loginVO){

				loginBO.setId(loginVO.getId());
				loginBO.setUserName(loginVO.getUserName());
				loginBO.setPassword(loginVO.getPassword());
				loginBO.setUserRole(loginVO.getUserRole());
				loginBO.setIsDelete(loginVO.isDelete());	
				loginBO.setEmailAddress(loginVO.getEmailAddress());
				loginBO.setPhotos(loginVO.getPhotos());

				
			}}catch (Exception e) {
				System.out.println(e);
				}

		return loginBO;
	}

	@Override
	public int insertUserDetails(LoginBO loginBO) {
		
		LoginVO loginVO=new LoginVO();
		
		int count=0;
		
		if(null!=loginBO){
			
			loginVO.setUserName(loginBO.getUserName());
			loginVO.setEmailAddress(loginBO.getEmailAddress());
			loginVO.setPassword(bCryptPasswordEncoder.encode(loginBO.getPassword()));
			loginVO.setMobileNumber(loginBO.getMobileNumber());
			loginVO.setAddress(loginBO.getAddress());
			loginVO.setUserRole("user");
			loginVO.setPhotos(loginBO.getPhotos());

			
			 count=loginDao.insertUserDetails(loginVO);

		}		
		return count;
	}

	@Override
	public boolean checkSameEmailId(String emailId) {
		
		boolean booleans=loginDao.checkSameEmailId(emailId);
		
		return booleans;
	}

	@Override
	public boolean checkMobileNumber(Long mobileNumber) {
		
		boolean booleans=loginDao.checkMobileNumber(mobileNumber);

		return booleans;
	}

	@Override
	public List<LoginBO> viewUserRegDetails() {
		
		List<LoginBO> loginBOlist=new ArrayList<LoginBO>();

		loginBOlist=loginDao.viewUserRegDetails();
		
/*		for(LoginVO loginVO:loginVOlist){
			
			LoginBO loginBO=new LoginBO();
			
			loginBO.setId(loginVO.getId());
			loginBO.setUserName(loginVO.getUserName());
			loginBO.setEmailId(loginVO.getEmailId());
			loginBO.setPassword(loginVO.getPassword());
			loginBO.setMobileNumber(loginVO.getMobileNumber());
			loginBO.setUserRole(loginVO.getUserRole());
			
			
			loginBOlist.add(loginBO);

		}
*/	
		return loginBOlist;
	}

	@Override
	public List<LoginBO> viewUserRegistrationDetails(int userid) {
		
		List<LoginBO> loginBOlist=new ArrayList<LoginBO>();

		loginBOlist=loginDao.viewUserRegistrationDetails(userid);
		
/*		for(LoginVO loginVO:loginVOlist){
			
			LoginBO loginBO=new LoginBO();
			
			loginBO.setId(loginVO.getId());
			loginBO.setUserName(loginVO.getUserName());
			loginBO.setEmailId(loginVO.getEmailId());
			loginBO.setPassword(loginVO.getPassword());
			loginBO.setMobileNumber(loginVO.getMobileNumber());
			loginBO.setUserRole(loginVO.getUserRole());
			
			loginBOlist.add(loginBO);

		}
		
*/		return loginBOlist;
	}

	@Override
	public LoginBO getUserDetailsssss(int userid) {
		
		LoginBO loginBO=new LoginBO();

		LoginVO loginVO=loginDao.getUserDetails(userid);
		
        if(null!=loginVO){		
		
			loginBO.setId(loginVO.getId());
			loginBO.setUserName(loginVO.getUserName());
			loginBO.setEmailAddress(loginVO.getEmailAddress());
			loginBO.setPassword(loginVO.getPassword());
			loginBO.setMobileNumber(loginVO.getMobileNumber());
			loginBO.setUserRole(loginVO.getUserRole());
			loginBO.setAddress(loginVO.getAddress());
		}		
		return loginBO;
	}

	@Override
	public int deleteProductDetails(LoginBO loginBO1) {
		
		LoginVO loginVO=new LoginVO();
		
		int count=0;
		
		if(null!=loginBO1){
			
			loginVO.setId(loginBO1.getId());
			loginVO.setUserName(loginBO1.getUserName());
			loginVO.setEmailAddress(loginBO1.getEmailAddress());
			loginVO.setPassword(loginBO1.getPassword());
			loginVO.setMobileNumber(loginBO1.getMobileNumber());
			loginVO.setAddress(loginBO1.getAddress());
			loginVO.setUserRole(loginBO1.getUserRole());
			
			count=loginDao.deleteProductDetails(loginVO);

		}	
		return count;
	}

	@Override
	public boolean userStatus(LoginBO loginBO) {
		
		boolean loginChanged = false;

		LoginVO loginVO = new LoginVO();

		if (0 != loginBO.getId()) {
			loginVO.setId(loginBO.getId());
			loginVO.setActive(loginBO.isIsActive());
			loginVO = loginDao.userStatus(loginVO);
			if (0 != loginVO.getId()) {
				loginChanged = true;
			}
		}		
		return false;
	}

	@Override
	public LoginVO postUsernameLogin(String username) {
		LoginVO loginVO = new LoginVO();
		if(null!=username) {
			loginVO.setUserName(username);
			loginVO = loginDao.postLogin(loginVO);
			if(null!=loginVO && 0<loginVO.getId()) {
				return loginVO;
			}
		}
		return loginVO;
	}

	

}


package com.scube.boot.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.scube.boot.bean.LoginBO;
import com.scube.boot.configuration.FileUploadUtil;
import com.scube.boot.service.LoginService;


@Controller
public class LoginController {


	@Autowired
	LoginService loginService;
	
	
	
	@RequestMapping(value="/",method = RequestMethod.GET)
	public String showHomePage(Model model,HttpServletRequest request){	
	

		model.addAttribute("loginBO",new LoginBO());
		request.setAttribute("userRole","admin");  
		request.setAttribute("userRole","default");  


		return "login";
	}
	
	@RequestMapping(value = "/homeAdmin", method = RequestMethod.GET)
	public String homePageAdmin(HttpServletRequest request) throws ServletException, IOException{

		request.setAttribute("userRole","admin");  

		return "default";
	}

	@RequestMapping(value = "/homeUser", method = RequestMethod.GET)
	public String homePageCustomer(HttpServletRequest request) throws ServletException, IOException{

		request.setAttribute("userRole","user");

		return "default";
	}



	@RequestMapping(value="/login",method = RequestMethod.GET)
	public String loginpage(Model model,HttpServletRequest request){

		model.addAttribute("loginBO", new LoginBO());
		request.setAttribute("userRole","admin"); 
		request.setAttribute("userRole","default");  

		return "login";		
	}

	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String loginpage(@ModelAttribute("loginBO")LoginBO loginBO,Model model,BindingResult result,HttpServletRequest request,HttpServletResponse responce){

		if(result.hasErrors()){
			return "login";
		}

		HttpSession session=request.getSession();

		LoginBO loginBO1=loginService.checkUserDetails(loginBO.getUserName(),loginBO.getPassword());

		if(null!=loginBO1&&0<loginBO1.getId()){

			String userrole=loginBO1.getUserRole();
			

			if(userrole.equals("admin")){

				request.setAttribute("userRole","admin");  

				int id=loginBO1.getId();

				session.setAttribute("userID", id);  
				
				session.setAttribute("userRole","admin");
			}

			else if(userrole.equals("user")){

				request.setAttribute("userRole","user");  

				int id=loginBO1.getId();

				session.setAttribute("userID", id);   
				
				session.setAttribute("userRole","user");

			}
			return "default";		


		}	
		
		if(null==loginBO1){

			String ErrorMsg = "User Does Not Exist!!"; 
			request.setAttribute("ErrorMsg",ErrorMsg);
			return "login";


		}
		else{
			String ErrorMsg = "User Does Not Exist!!"; 
			request.setAttribute("ErrorMsg",ErrorMsg);
			return "login";
		}
	}
	
	
	@RequestMapping(value="/userReg",method=RequestMethod.GET)
	public String getUserReg(Model model,HttpServletRequest request){
		
		model.addAttribute("userReg",new LoginBO());
		request.setAttribute("userRole","default");  

				
		return "user_registration";		
	}
	
	@RequestMapping(value="/userReg",method=RequestMethod.POST)
	public String postUserReg(@ModelAttribute("userReg")LoginBO loginBO,@RequestParam("image") MultipartFile multipartFile,BindingResult result,HttpServletRequest request,HttpServletResponse response) throws IOException{

		HttpSession session=request.getSession();
						
		boolean booleans=loginService.checkSameEmailId(loginBO.getEmailAddress());
		
		if(booleans==false){
			
			String ErrorMsg = "Email Id already exist !!"; 
			session.setAttribute("ErrorMsg",ErrorMsg);
			return "user_registration";			
		}

		
		if(booleans){
		
		boolean booleanss=loginService.checkMobileNumber(loginBO.getMobileNumber());

		if(booleans==false){
			
			String ErrorMsg = "Mobile Nmuber already exist !!"; 
			session.setAttribute("ErrorMsg",ErrorMsg);
			return "user_registration";			
		}

		
		if(booleanss){
			
		
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
	        loginBO.setPhotos(fileName);
		
		int count=loginService.insertUserDetails(loginBO);
		
        String uploadDir = "user-photos/" + count;
        
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);   
        
		
  	    if(0<count){

			request.setAttribute("userRole","admin");  
			
			return "redirect:/login";
		}}}
		 
		else{
			String ErrorMsg = "User Reistration update Unsucessfully!!"; 
			session.setAttribute("ErrorMsg",ErrorMsg);
			return "user_registration";
		}
		return "user_registration";

	}
	
	
@RequestMapping(value="/getviewreg",method =RequestMethod.GET)
public String viewUserDetails(HttpServletRequest request,HttpServletResponse response){

	List<LoginBO> list=new ArrayList<LoginBO>();

	list=loginService.viewUserRegDetails();
	
	if(null!=list && 0<list.size()){
		request.setAttribute("listref", list);

		request.setAttribute("userRole","admin");
	}

	return "view_users_details";
}


@RequestMapping(value="/getViewuser",method =RequestMethod.GET)
public String viewUserRegistration(HttpServletRequest request,HttpServletResponse response,Model model){

	HttpSession session=request.getSession();
	int userid=(int) session.getAttribute("userID");//user id

	List<LoginBO> list=new ArrayList<LoginBO>();

	list=loginService.viewUserRegistrationDetails(userid);
	
		
	if(null!=list && 0<list.size()){
		request.setAttribute("listref", list);

		request.setAttribute("userRole","user");
	}

	return "view_users_details";
}


@RequestMapping(value="/getdeleteregistration",method=RequestMethod.GET)
public String deleteProductRegistration(Model model,LoginBO loginBO,HttpServletRequest request,HttpServletResponse response){

	String userid=request.getParameter("id");
	
	
	if(null!=userid && !userid.isEmpty()){
		int UserID = Integer.parseInt(userid);
		

		LoginBO loginBO1=loginService.getUserDetailsssss(UserID);

		int count=loginService.deleteProductDetails(loginBO1);

		request.setAttribute("userRole","admin");  
	}
	return "redirect:/getviewreg";
}


@RequestMapping(value = "/active-deactive-user", method = RequestMethod.GET)
public String activedeactiveuser(Model model, HttpServletRequest request)
		throws FileNotFoundException {
	try {
		
		String status = request.getParameter("status");
		String statusvalue[] = status.split(",");
		String userstatus = statusvalue[0];
		int userId = Integer.valueOf(statusvalue[1]);

		LoginBO loginBO = new LoginBO();
		
		loginBO.setId(userId);
		if (userstatus.equals("Active")) {
			loginBO.setIsActive(false);
		} else {
			loginBO.setIsActive(true);
		}

		boolean useractivestatus = loginService.userStatus(loginBO);

		if (useractivestatus) {
			model.addAttribute("successmessage", "Admin user activated Successfully");
		} else {
			model.addAttribute("successmessage", "Admin user de-activated Successfully");
		}
	} catch (final NullPointerException ne) {
		ne.printStackTrace();
	}

	return "redirect:/getviewreg";
}


}	




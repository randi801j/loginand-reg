package com.randyluc.LoginAndReg.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.randyluc.LoginAndReg.models.LoginUser;
import com.randyluc.LoginAndReg.models.User;
import com.randyluc.LoginAndReg.services.UserServ;


@Controller
public class MainController {
 
 
  @Autowired
  private UserServ userServ;
 
 @GetMapping("/")
 public String index(Model model) {
 
     // Bind empty User and LoginUser objects to the JSP
     // to capture the form input
     model.addAttribute("newUser", new User());
     model.addAttribute("newLogin", new LoginUser());
     return "index.jsp";
 }
 
 @PostMapping("/register")
 public String register(@Valid @ModelAttribute("newUser") User newUser, 
         BindingResult result, Model model, HttpSession session) {
     
     // TO-DO Later -- call a register method in the service 
     // to do some extra validations and create a new user!
	 userServ.register(newUser,result);
     
     if(result.hasErrors()) {
         // Be sure to send in the empty LoginUser before 
         // re-rendering the page.
         model.addAttribute("newLogin", new LoginUser());
         return "index.jsp";
     }
     
     // No errors! 
     // TO-DO Later: Store their ID from the DB in session, 
     // in other words, log them in.
     session.setAttribute("userId", newUser.getId());
     return "redirect:/welcome";
 }
 
 @PostMapping("/login")
 public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, 
         BindingResult result, Model model, HttpSession session) {
     
     // Add once service is implemented:
     User user = userServ.login(newLogin, result);
 
     if(result.hasErrors()) {
         model.addAttribute("newUser", new User());
         return "index.jsp";
     }
 
     // No errors! 
     // TO-DO Later: Store their ID from the DB in session, 
     // in other words, log them in.
     session.setAttribute("userId", user.getId());
     return "redirect:/welcome";
 }
 
 @GetMapping ("/logout")
 public String logout(HttpSession session) {
	 session.setAttribute("userId", null);
	 return "redirect:/";
 }
 
 @GetMapping ("/welcome")
 public String welcome(Model model, HttpSession session) {
	 Long userId=(Long)session.getAttribute("userId");
	 if(userId== null) {
		 return "redirect:/";
	}
	User user = userServ.findById(userId);
	model.addAttribute("user",user);
	return "Dashboard.jsp";
 }
 
}


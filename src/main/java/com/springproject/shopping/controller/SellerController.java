package com.springproject.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springproject.shopping.model.Seller;
import com.springproject.shopping.service.SellerService;

@Controller
public class SellerController {
	@Autowired
	private SellerService sellerService;
	BCryptPasswordEncoder encoder= new BCryptPasswordEncoder();
	
	@GetMapping("/seller-signup")
	public String signUp() {
		
		
		return "sellerSignup";
	}
	@PostMapping("/seller-signup")
	public String postSignup(@ModelAttribute Seller seller, Model model) {
		if (seller.getPassword().equals(seller.getPassword2())) {
			try {
				String hashedPassword = encoder.encode(seller.getPassword());
				seller.setPassword(hashedPassword);
				// this sends the data to signup service while receiving if and which data
				// already exist in database
				String dupError = sellerService.sellerSignup(seller);
				if (dupError == null) {
					return "sellerLogin";
				}
				// Add specific error messege this way cause i was not able to extract that
				// information from exception
				model.addAttribute("dupError", dupError + " already exists");
				return "SellerSignup";
			} catch (DataIntegrityViolationException e) {
				// Add error message to the model
				model.addAttribute("dupError", "Some info you entered already exists, try new one");
				return "redirect:/seller-signup";
			}
		}
		model.addAttribute("error", "Passwords do not match");
		return "sellerSignup";
	}
	@GetMapping("/seller-login")
	public String sellerLogin() {
		
		return "sellerLogin";
	}
	@PostMapping("seller-login")
	public String postSellerLogin(Model model,@RequestParam String email,@RequestParam String password) {
		Seller seller= sellerService.findSellerByEmail(email);
		if(sellerService.verifyPassword(password, seller, encoder)) {
			 
			return "sellerDashboard";
		}
		
		
		
		return "";
	}

}

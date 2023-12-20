package com.springproject.shopping.controller;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.springproject.shopping.model.Admin;
import com.springproject.shopping.service.AdminService;

@Controller
public class AdminController {
	@Autowired
	private AdminService adminService;
	// Starting the use of BCrypt password encoder
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	@GetMapping({ "/", "/adminlogin" })
	public String getAdminLogin() {

		return "AdminLogin";
	}

	@PostMapping("/adminlogin")
	public String postLogin(@RequestParam("email") String email, @RequestParam("password") String password,
			Model model) {
		// Retrieve the user by email from the database
		Admin admin = adminService.findAdminByEmail(email);
		if (admin != null && bCryptPasswordEncoder.matches(password, admin.getPassword())) {
			// Successful login
			model.addAttribute("admin", admin);
			return "AdminPanel";
		} else {
			// Failed login
			model.addAttribute("error", "Invalid email or password");
			return "Adminlogin";
		}
	}

	@GetMapping("/adminSignup")
	public String getAdminSignup() {

		return "AdminSignup";
	}

	@PostMapping("/adminSignup")
	public String postSignup(@ModelAttribute Admin admin, @RequestParam("image") MultipartFile imageFile)
	// requestparam and multipart file are used to receive byte data like image to
	// the server
	{
		// Hashing the password
		String hashedPassword = bCryptPasswordEncoder.encode(admin.getPassword());
		admin.setPassword(hashedPassword);
		//
		byte[] profilePicture;
		try {
			profilePicture = imageFile.getBytes();
			admin.setProfilePicture(profilePicture);
		} catch (IOException e) {

			e.printStackTrace();
		}

		// this sends the data to signup service
		adminService.adminSignup(admin);
		return "AdminLogin";
	}

	@GetMapping("/home")
	public String home() {

		return "AdminPanel";
	}

	@GetMapping("/employees")
	public String employees() {

		return "Employees";
	}

	@GetMapping("sellers")
	public String sellers() {

		return "Sellers";
	}

	@GetMapping("/users")
	public String users() {

		return "Users";
	}

	@GetMapping("/requests")
	public String requests() {
		return "Requests";
	}
	 @SuppressWarnings({ "deprecation", "removal" })
	@GetMapping("/admin")
	    public String admin(Model model) {
	        List<Admin> adminList = adminService.getAllAdmin();

	        // Convert profile pictures to Base64
	        adminList.forEach(admin -> {
	            if (admin.getProfilePicture() != null) {
	                String base64Image = Base64Utils.encodeToString(admin.getProfilePicture());
	                admin.setProfilePictureBase64(base64Image);
	            }
	        });

	        model.addAttribute("adminList", adminList);
	        return "Admins";
	    }

	@PostMapping("/admin")
	public String adminList() {

		return "Admins";
	}
}

package com.springproject.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.springproject.shopping.model.Admin;
import com.springproject.shopping.service.AdminService;

import jakarta.servlet.http.HttpSession;

@SuppressWarnings("removal")
@Controller
public class AdminController {
	@Autowired
	private AdminService adminService;
	// Starting the use of BCrypt password encoder
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	@GetMapping({ "/", "/adminlogin" })
	public String getAdminLogin(HttpSession session) {
		if (session.getAttribute("activeAdmin") != null) {
			session.invalidate();
		}
		return "AdminLogin";
	}

	@PostMapping("/adminlogin")
	public String postLogin(@RequestParam("email") String email, @RequestParam("password") String password, Model model,
			// Creating session
			jakarta.servlet.http.HttpSession session, @RequestParam(name="rememberMe",required=false,defaultValue="false") boolean rememberMe) {
		// Retrieve the user by email from the database
		Admin admin = adminService.findAdminByEmail(email);
		if (admin != null && bCryptPasswordEncoder.matches(password, admin.getPassword())) {
			// Successful login
			// properly setting session
			session.setAttribute("activeAdmin", admin);
			if(rememberMe==false) {
				session.setMaxInactiveInterval(20);
			}
				if (admin.getProfilePicture() != null) {
					// Converting the byte array of profile picture to base64
					@SuppressWarnings("deprecation")
					String base64Image = Base64Utils.encodeToString(admin.getProfilePicture());
					admin.setProfilePictureBase64(base64Image);
				
			}
			model.addAttribute("fname", admin.getFirstName());
			model.addAttribute("lname",admin.getLastName());
			model.addAttribute("profilePicture", admin.getProfilePictureBase64());
			model.addAttribute("admin", admin);
			return "Dashboard";
		} else {
			// Failed login
			model.addAttribute("error", "Invalid email or password");
			return "Adminlogin";
		}
	}

	@GetMapping("/adminSignup")
	public String getAdminSignup(HttpSession session) {
		if (session.getAttribute("activeAdmin") != null) {
			session.invalidate();
		}
		return "AdminSignup";
	}

	@PostMapping("/adminSignup")
	public String postSignup(@ModelAttribute Admin admin, Model model, @RequestParam String email,
			@RequestParam String firstName, @RequestParam String lastName, @RequestParam String password,
			@RequestParam String phone) {
		// MultipartFile imageFile, @RequestParam("image"))
		// requestparam and multipart file are used to receive byte data like image to
		// the server

		// Hashing the password
		if (admin.getPassword().equals(admin.getPassword2())) {
			try {
				String hashedPassword = bCryptPasswordEncoder.encode(admin.getPassword());
				admin.setPassword(hashedPassword);
				//
//		byte[] profilePicture;
//		try {
//			profilePicture = imageFile.getBytes();
//			admin.setProfilePicture(profilePicture);
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}

				// this sends the data to signup service while receiving if and which data
				// already exist in database
				String dupError = adminService.adminSignup(admin);
				if (dupError == null) {
					return "AdminLogin";
				}
				model.addAttribute("dupError", dupError + " already exists");
				return "AdminSignup";
			} catch (DataIntegrityViolationException e) {
				// Add error message to the model
				model.addAttribute("dupError", "Some info you entered already exists, try new one");
				return "redirect:/adminSignup";
			}
		}
		model.addAttribute("error", "Passwords do not match");
		return "AdminSignup";
	}

	@GetMapping("/home")
	public String home(HttpSession session) {
		if (session.getAttribute("activeAdmin") != null) {
			return "AdminPanel";
		}
		return "AdminLogin";
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

	@SuppressWarnings("deprecation")
	@GetMapping("/admin")
	public String admin(Model model, HttpSession session) {
		// Verifying activeAdmin
		if (session.getAttribute("activeAdmin") != null) {
			List<Admin> adminList = adminService.getAllAdmin();

			// Defining for_each loop to convert longblob image to normal one
//		adminList.forEach(admin -> {
//			if (admin.getProfilePicture() != null) {
//				// Converting the byte array of profile picture to base64
//				String base64Image = Base64Utils.encodeToString(admin.getProfilePicture());
//				admin.setProfilePictureBase64(base64Image);
//			}
//		});
			model.addAttribute("adminList", adminList);
			return "Admins";
		}
		return "AdminLogin";

	}

	@PostMapping("/admin")
	public String adminList(HttpSession session) {
		if (session.getAttribute("activeAdmin") != null) {
			return "Admins";
		}

		return "AdminLogin";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		// Destroying the session
		session.invalidate();
		return "AdminLogin";
	}

	@GetMapping("/admin/delete")
	public String delete(@RequestParam int id) {
		adminService.deleteAdmin(id);
		return "redirect:/admin";
	}

	@GetMapping("/admin/edit")
	public String edit() {

		return "AdminEdit";
	}
}

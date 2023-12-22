package com.springproject.shopping.service;

import java.util.List;

import com.springproject.shopping.model.Admin;

public interface AdminService {
	String adminSignup(Admin admin);

	Admin findAdminByEmail(String email);

	List<Admin> getAllAdmin();
	void deleteAdmin(int id);
}

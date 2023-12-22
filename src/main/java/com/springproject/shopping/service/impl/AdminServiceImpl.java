package com.springproject.shopping.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.shopping.model.Admin;
import com.springproject.shopping.repositoty.AdminRepository;
import com.springproject.shopping.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private AdminRepository adminRepo;

	@Override
	public String adminSignup(Admin admin) {

		if (adminRepo.existsByEmail(admin.getEmail())) {
			return "Email";
		}
		if (adminRepo.existsByPhone(admin.getPhone())) {
			return "Phone";
		}

		// this saves the data to the database
		adminRepo.save(admin);
		return null;
	}

	@Override
	public Admin findAdminByEmail(String email) {
		return adminRepo.findByEmail(email);
	}

	@Override
	public List<Admin> getAllAdmin() {

		return adminRepo.findAll();
	}

	@Override
	public void deleteAdmin(int id) {
		adminRepo.deleteById(id);

	}

}

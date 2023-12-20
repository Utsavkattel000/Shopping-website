package com.springproject.shopping.repositoty;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springproject.shopping.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
	Admin findByEmail(String email);
}

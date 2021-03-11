package com.example.springBootTest.auth;

import java.util.Optional;

public interface ApplicationUserDAO {

	public Optional<ApplicationUser> selectApplicationUserByName(String username);
}

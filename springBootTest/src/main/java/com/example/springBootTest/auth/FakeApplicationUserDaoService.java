package com.example.springBootTest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.beans.Encoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.springBootTest.security.AppUserRoles.*;

@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDAO{

	private final PasswordEncoder passwordEncoder;

	@Autowired
	public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Optional<ApplicationUser> selectApplicationUserByName(String username) {
		return getApplicationUsers()
				.stream()
				.filter(applicationUser -> applicationUser.getUsername().equals(username))
				.findFirst();
	}

	private List<ApplicationUser> getApplicationUsers(){
		List<ApplicationUser> listOfUsers = new ArrayList<ApplicationUser>();
		listOfUsers.add(new ApplicationUser(ADMIN.getGrantedAuthority(),
						"Valeriy Zhmyshenko",
						passwordEncoder.encode("vamban"),
						true,
						true,
						true,
						true));
		listOfUsers.add(new ApplicationUser(ADMINTRAINEE.getGrantedAuthority(),
						"Jonny",
						passwordEncoder.encode("diearasaka"),
						true,
						true,
						true,
						true));
		listOfUsers.add(new ApplicationUser(STUDENT.getGrantedAuthority(),
						"Claptrap",
						passwordEncoder.encode("hook123"),
						true,
						true,
						true,
						true));

		return listOfUsers;
	}
}

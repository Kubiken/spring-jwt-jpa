package com.example.springBootTest.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.springBootTest.security.AppUserPermissions.*;

public enum AppUserRoles {
	STUDENT(Sets.newHashSet()),
	ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE,
			STUDENT_READ, STUDENT_WRITE)),
	ADMINTRAINEE(Sets.newHashSet(STUDENT_READ, COURSE_READ));

	private final Set<AppUserPermissions> permissions;

	AppUserRoles(Set<AppUserPermissions> permissions) {
		this.permissions = permissions;
	}

	public Set<AppUserPermissions> getPermissions(){return permissions;}

	public Set<SimpleGrantedAuthority> getGrantedAuthority(){
	Set<SimpleGrantedAuthority> permission = getPermissions().stream()
				.map(permissions -> new SimpleGrantedAuthority(permissions.getPermission()))
				.collect(Collectors.toSet());
	permission.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
	return permission;
	}
}

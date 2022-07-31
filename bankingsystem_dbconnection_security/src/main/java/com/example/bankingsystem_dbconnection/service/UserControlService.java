package com.example.bankingsystem_dbconnection.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.bankingsystem_dbconnection.model.AccountUser;
import com.example.bankingsystem_dbconnection.model.AccountUserDetails;
import com.example.bankingsystem_dbconnection.repository.IMyBatisUserRepository;

@Component
public class UserControlService implements UserDetailsService {

	private IMyBatisUserRepository mybatisUserRepository;

	@Autowired
	public UserControlService(IMyBatisUserRepository mybatisUserRepository) {
		this.mybatisUserRepository = mybatisUserRepository;
	}

	//Loading user detail with given username parameter and getting authorities of usersF
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		AccountUser userDetail = mybatisUserRepository.getUserWithName(username);
		String userpass = userDetail.getPassword();
		String allAuthorities = userDetail.getAuthorities();
		String[] commaSeperatedAuthority = allAuthorities.split(",");
		List<GrantedAuthority> pAuthority = new ArrayList<GrantedAuthority>();
		for (int k = 0; k < commaSeperatedAuthority.length; k++) {
			SimpleGrantedAuthority aut = new SimpleGrantedAuthority(commaSeperatedAuthority[k]);
			pAuthority.add(aut);
		}

		AccountUserDetails aud = new AccountUserDetails(userDetail.getId(), userDetail.getUsername(),
				userDetail.getPassword(), true, true, true, true, pAuthority);
		return aud;

	}
}

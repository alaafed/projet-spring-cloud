package org.sid.secservice.sec.service;

import org.sid.secservice.sec.entities.AppRole;
import org.sid.secservice.sec.entities.AppUser;

import java.util.List;

public interface AccountService {
    AppUser addNewUser (AppUser appUser);
    AppRole addnewRole (AppRole approle);
    void addRolToUser(String username,String rolename);
    AppUser loadUserByusername(String username);
    List<AppUser> listUser();
}

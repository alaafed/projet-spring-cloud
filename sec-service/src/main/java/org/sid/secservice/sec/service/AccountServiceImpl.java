package org.sid.secservice.sec.service;

import org.sid.secservice.sec.entities.AppRole;
import org.sid.secservice.sec.entities.AppUser;
import org.sid.secservice.sec.repo.AppRoleRepository;
import org.sid.secservice.sec.repo.AppuserRpository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private AppuserRpository appuserRpository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AppuserRpository appuserRpository, AppRoleRepository appRoleRepository, PasswordEncoder passwordEncoder) {
        this.appuserRpository = appuserRpository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser addNewUser(AppUser appUser) {
        String pw=appUser.getPassword();
        appUser.setPassword(passwordEncoder.encode(pw));
        return appuserRpository.save(appUser);
    }

    @Override
    public AppRole addnewRole(AppRole approle) {

        return appRoleRepository.save(approle);
    }

    @Override
    public void addRolToUser(String username, String rolename) {
        AppUser appUser =appuserRpository.findByusername(username);
        AppRole appRole= appRoleRepository.findByRolename(rolename);
        appUser.getAppRoles().add(appRole);

    }

    @Override
    public AppUser loadUserByusername(String username) {
        return appuserRpository.findByusername(username);
    }

    @Override
    public List<AppUser> listUser() {

        return appuserRpository.findAll();
    }
}

package org.sid.secservice.sec.repo;

import org.sid.secservice.sec.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppuserRpository extends JpaRepository <AppUser,Long> {
    AppUser findByusername(String username);
}

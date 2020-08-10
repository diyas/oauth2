package com.fp.oauth2.configuration.services.repository;

import com.fp.oauth2.configuration.services.domain.UserView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserViewRepo extends JpaRepository<UserView, Long> {
    public UserView findByUsername(String username);
}

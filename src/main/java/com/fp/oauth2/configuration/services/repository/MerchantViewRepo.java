package com.fp.oauth2.configuration.services.repository;

import com.fp.oauth2.configuration.services.domain.MerchantView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantViewRepo extends JpaRepository<MerchantView, Long> {

}

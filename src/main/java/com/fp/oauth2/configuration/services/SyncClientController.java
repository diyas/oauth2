package com.fp.oauth2.configuration.services;

import com.fp.oauth2.configuration.services.domain.MerchantView;
import com.fp.oauth2.configuration.services.domain.OauthClientDetails;
import com.fp.oauth2.configuration.services.repository.MerchantViewRepo;
import com.fp.oauth2.configuration.services.repository.OauthClientRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/sync")
public class SyncClientController {

    @Autowired
    private MerchantViewRepo merchantViewRepo;

    @Autowired
    private OauthClientRepo clientRepo;

    @PatchMapping("/client")
    public String syncClient(){
        List<OauthClientDetails> clients = new ArrayList<>();
        List<MerchantView> merchants = merchantViewRepo.findAll();
        int countInsert = 0;
        int countUpdate = 0;
        for (MerchantView mv:merchants){
            OauthClientDetails o = clientRepo.findByClientId(String.valueOf(mv.getMerchantId()));
            if (o == null){
                o = new OauthClientDetails();
                o.setClientId("client-"+String.valueOf(mv.getMerchantId()));
                o.setResourceIds("rest-api");
                o.setClientSecret("$2y$12$pgHlh7m6No0TBRdfYNeTLeXx5qCtDATcrQzsGg3mRbm3KKHTWTIju");
                o.setScope("read,write");
                o.setAuthorizedGrantTypes("password,authorization_code,refresh_token");
                o.setWebServerRedirectUri("");
                o.setAuthorities("");
                o.setAccessTokenValidity(500);
                o.setRefreshTokenValidity(520);
                o.setAdditionalInformation("");
                o.setAutoApprove(true);
                countInsert = countInsert + 1;
            } else {
                countUpdate = countUpdate + 1;
            }
            clients.add(o);
        }
        log.info("New Client: "+countInsert);
        log.info("Update Client: "+countUpdate);
        clientRepo.saveAll(clients);
        return "OK";
    }
}

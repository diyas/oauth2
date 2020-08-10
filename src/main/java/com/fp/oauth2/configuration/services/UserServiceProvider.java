package com.fp.oauth2.configuration.services;

import com.fp.oauth2.configuration.services.domain.UserView;
import com.fp.oauth2.configuration.services.repository.UserViewRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mariadb.jdbc.internal.com.send.authentication.ed25519.Utils.bytesToHex;

@Service
public class UserServiceProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    UserViewRepo userRepo;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        User user = (User) userDetails;
        Map req = (Map) authentication.getDetails();
        if (!user.isAccountNonExpired()) {
            throw new AccountNotConfirmedException("Account is not confirmed yet.");
        }

        if (authentication.getCredentials() == null) {
            logger.warn("Authentication failed: no credentials provided");
            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"), null);
        }

        logger.info("Details: "+authentication.getDetails());

        Map mapLogin = checkPasswordWithTimestamp(user.getPassword(), authentication.getCredentials().toString(), req.get("salt_password").toString());
        if (!(Boolean) mapLogin.get("isMatch")) {
            logger.warn("Authentication failed: password does not match stored value");
            throw new BadCredentialsException(messages.getMessage("UserDetailsAuthenticationProviderImpl.badCredentials",
                    "Bad credentials"), null);
        }
    }

    public static class AccountNotConfirmedException extends AuthenticationException {
        public AccountNotConfirmedException(String message) {
            super(message);
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserView user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new User(String.valueOf(user.getUsername()), user.getPassword(), getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public Map checkPasswordWithTimestamp(String passFromDb, String passRequest, String timeStamp) {
        boolean returnValue = false;
        Map returnMap = new HashMap();
        try {
            MessageDigest mdSHA256 = MessageDigest.getInstance("SHA-256");
            byte[] baPasswdHashdb = mdSHA256.digest((timeStamp+passFromDb).getBytes(StandardCharsets.UTF_8));
            String strPasswdHashdb = bytesToHex(baPasswdHashdb).toLowerCase();
            returnMap.put("passHashDb", strPasswdHashdb);
            if (passRequest.equals(strPasswdHashdb))
                returnValue = true;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        returnMap.put("isMatch", returnValue);
        return returnMap;
    }
}

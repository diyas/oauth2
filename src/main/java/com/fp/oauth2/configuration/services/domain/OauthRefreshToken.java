package com.fp.oauth2.configuration.services.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Data
@Entity
@Table
public class OauthRefreshToken {
    @Id
    @Column
    private String tokenId;
    @Column
    private byte[] token;
    @Column
    private byte[] authentication;
}

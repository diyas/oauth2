
package com.fp.oauth2.configuration.services.domain;

import jdk.nashorn.internal.ir.annotations.Immutable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Immutable
@Table
public class UserView {

    @Id
    @Column
    private Integer id;
    @Column
    private String username;
    @Column
    private String password;
    @Column(name="merchant_id")
    private Integer merchantId;
    @Column(name="lock_flag")
    private String lockFlag;

}

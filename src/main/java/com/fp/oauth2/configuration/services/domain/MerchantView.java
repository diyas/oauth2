package com.fp.oauth2.configuration.services.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "merchant_view")
public class MerchantView {
    @Id
    @Column
    private int merchantId;
    @Column
    private String merchantGroupName;
    @Column
    private String merchantName;
}

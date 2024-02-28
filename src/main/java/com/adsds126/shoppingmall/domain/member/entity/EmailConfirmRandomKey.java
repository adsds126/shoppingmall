package com.adsds126.shoppingmall.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EMAIL_CONFIRM_RANDOM")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailConfirmRandomKey {
    @Id
    private String email;
    @Column
    private String randomKey;
}

package com.markets.demo.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty
    @Size(max = 100)
    @Column(unique = true)
    private String arabicName;

    @NotEmpty
    @Size(max = 100)
    @Column(unique = true)
    private String englishName;

    @Lob
    @NotEmpty
    private String address;

    @Lob
    private byte[] image;

    private boolean isActive;

}

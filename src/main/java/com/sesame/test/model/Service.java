package com.sesame.test.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;


/**
 * Service model
 */
@Builder
@EqualsAndHashCode
@ToString
@Getter
public class Service {

    @NotBlank(message = "serviceNameNotBlank")
    private String serviceName;

    @Positive(message = "pricePositive")
    private int price;
}

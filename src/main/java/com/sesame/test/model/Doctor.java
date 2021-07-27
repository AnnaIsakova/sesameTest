package com.sesame.test.model;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Doctor model
 */
@Builder
@EqualsAndHashCode(exclude = {"appointmentsByLocation"})
@ToString
@Getter
@With
public class Doctor {

    @NotBlank(message = "firstNameNotBlank")
    private String firstName;

    @NotBlank(message = "lastNameNotBlank")
    private String lastName;

    @Valid
    private List<Location> appointmentsByLocation;
}

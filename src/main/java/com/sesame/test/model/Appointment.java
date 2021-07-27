package com.sesame.test.model;

import lombok.*;
import org.hibernate.validator.constraints.time.DurationMin;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.ZonedDateTime;


/**
 * Appointment model
 */
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Getter
public class Appointment {

    @NotBlank(message = "appointmentIdNotBlank")
    @EqualsAndHashCode.Include
    private String appointmentId;

    @NotNull(message = "dateTimeNotNull")
    private ZonedDateTime startDateTime;

    @DurationMin(minutes = 1, message = "durationPositive")
    private Duration duration;

    @NotNull
    @Valid
    private Service service;

}

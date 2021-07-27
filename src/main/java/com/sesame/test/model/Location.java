package com.sesame.test.model;


import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Location model
 */
@Builder
@EqualsAndHashCode(exclude = {"appointments"})
@ToString
@Getter
@With
public class Location {

    @NotBlank(message = "locationNameNotBlank")
    private String locationName;

    @Valid
    List<Appointment> appointments;
}

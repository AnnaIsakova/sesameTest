package com.sesame.test.dto;

import com.sesame.test.model.Doctor;
import com.sesame.test.model.Violation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


/**
 * DTO that represent complex Doctor response
 */
@Data
@AllArgsConstructor
public class DoctorsComplexResponse {

    private List<Doctor> doctors;
    private List<Violation> violations;
}

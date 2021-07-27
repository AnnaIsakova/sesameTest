package com.sesame.test.controller;

import com.sesame.test.dto.DoctorsComplexResponse;
import com.sesame.test.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Contains methods that provide Doctor specific endpoints
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    /**
     * Exposes GET /doctors endpoint
     * @return complex response that contains Doctors list and Appointment Ids with corrupted data
     */
    @GetMapping
    public ResponseEntity<DoctorsComplexResponse> getDoctors() {
        return new ResponseEntity<>(doctorService.getDoctors(), HttpStatus.OK);
    }

}

package com.sesame.test.client;

import lombok.Data;

/**
 * Model class that corresponds to Appointment object from Sesame Api
 */
@Data
public class AppointmentApi {

    private String id;
    private DoctorApi doctor;
    private int durationInMinutes;
    private String time;
    private ServiceApi service;
    private LocationApi location;

}

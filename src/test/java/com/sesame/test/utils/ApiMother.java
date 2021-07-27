package com.sesame.test.utils;

import com.sesame.test.client.AppointmentApi;
import com.sesame.test.client.DoctorApi;
import com.sesame.test.client.LocationApi;
import com.sesame.test.client.ServiceApi;


public class ApiMother {

    static final String FIRST_NAME = "John";
    static final String LAST_NAME = "Doe";
    static final String APPOINTMENT_ID = "app_id";
    static final int DURATION_MINUTES = 10;
    static final String TIME = "2021-07-22 11:45:00";
    static final String LOCATION_NAME = "Medical Centre";
    static final String TIME_ZONE = "America/Chicago";
    static final String SERVICE_NAME = "X-Ray";
    static final int PRICE = 50;

    public static AppointmentApi validAppointment() {
        var appointment = new AppointmentApi();
        appointment.setId(APPOINTMENT_ID);
        appointment.setTime(TIME);
        appointment.setDurationInMinutes(DURATION_MINUTES);
        appointment.setDoctor(validDoctor());
        appointment.setLocation(validLocation());
        appointment.setService(validService());

        return appointment;
    }

    public static DoctorApi validDoctor() {
        var doctor = new DoctorApi();
        doctor.setFirstName(FIRST_NAME);
        doctor.setLastName(LAST_NAME);
        return doctor;
    }

    public static LocationApi validLocation() {
        var location = new LocationApi();
        location.setName(LOCATION_NAME);
        location.setTimeZoneCode(TIME_ZONE);
        return location;
    }

    public static ServiceApi validService() {
        var service = new ServiceApi();
        service.setName(SERVICE_NAME);
        service.setPrice(PRICE);
        return service;
    }

}

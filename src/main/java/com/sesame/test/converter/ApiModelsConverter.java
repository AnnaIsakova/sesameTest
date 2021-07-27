package com.sesame.test.converter;

import com.sesame.test.client.AppointmentApi;
import com.sesame.test.exception.ConstraintException;
import com.sesame.test.model.Appointment;
import com.sesame.test.model.Doctor;
import com.sesame.test.model.Location;
import com.sesame.test.model.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Responsible for converting Sesame Api objects into application models
 */
public class ApiModelsConverter {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Returns Doctor model based on Sesame Api Appointment object
     * Validates Doctor model
     * Exception is thrown if doctor validation fails
     * @param appointment - Sesame Api Appointment object
     * @return Doctor application model
     * @throws ConstraintException
     */
    public static Doctor convertAppointmentToDoctor(AppointmentApi appointment) throws ConstraintException {
        var dateTime = getDateTime(appointment.getTime(), appointment.getLocation().getTimeZoneCode());

        var service = Service.builder()
                .serviceName(appointment.getService().getName())
                .price(appointment.getService().getPrice())
                .build();

        var app = Appointment.builder()
                .appointmentId(appointment.getId())
                .service(service)
                .duration(Duration.ofMinutes(appointment.getDurationInMinutes()))
                .startDateTime(dateTime)
                .build();

        var location = Location.builder()
                .locationName(appointment.getLocation().getName())
                .appointments(List.of(app))
                .build();

        var doctor = Doctor.builder()
                .firstName(appointment.getDoctor().getFirstName())
                .lastName(appointment.getDoctor().getLastName())
                .appointmentsByLocation(List.of(location))
                .build();

        validateDoctor(doctor);

        return doctor;
    }

    private static ZonedDateTime getDateTime(String dateTime, String timeZoneCode) {
        var zone = ZoneId.of(timeZoneCode);
        var fmt = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).withZone(zone);
        try {
            return ZonedDateTime.parse(dateTime, fmt);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static void validateDoctor(Doctor doctor) throws ConstraintException {
        var violations = validator.validate(doctor);
        if (!violations.isEmpty()) {
            var violationMessages = violations
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toList());
            throw new ConstraintException(violationMessages);
        }
    }
}

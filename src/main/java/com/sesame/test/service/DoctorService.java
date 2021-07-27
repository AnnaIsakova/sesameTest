package com.sesame.test.service;

import com.sesame.test.client.SesameClient;
import com.sesame.test.dto.DoctorsComplexResponse;
import com.sesame.test.exception.ConstraintException;
import com.sesame.test.model.Doctor;
import com.sesame.test.model.Location;
import com.sesame.test.model.Violation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sesame.test.converter.ApiModelsConverter.convertAppointmentToDoctor;


@RequiredArgsConstructor
@Service
public class DoctorService {

    private final SesameClient sesameClient;

    /**
     * Uses Sesame client to fetch Appointments
     * Converts Appointments list from Api into Doctors list
     * Puts all appointments of one doctor into Doctor.appointmentsByLocation
     * @return DoctorsComplexResponse
     */
    public DoctorsComplexResponse getDoctors() {
        var appointments = sesameClient.getAppointments();
        var doctors = new HashMap<Doctor, Doctor>();
        var violationsMap = new HashMap<String, Violation>();

        appointments.forEach(appointment -> {
            try {
                var doctor = convertAppointmentToDoctor(appointment);
                doctors.merge(doctor, doctor, this::getUpdatedDoctor);
            } catch (ConstraintException e) {
                var ids = List.of(appointment.getId());
                var violations = e.getViolations()
                        .stream()
                        .map(v -> new Violation(v, ids));
                violations.forEach(v -> {
                    violationsMap.merge(v.getRule(), v, this::getUpdatedViolation);
                });
            }
        });

        return new DoctorsComplexResponse(new ArrayList<>(doctors.values()), new ArrayList<>(violationsMap.values()));
    }

    // Returns violation with merged entityIds
    private Violation getUpdatedViolation(Violation oldViolation, Violation newViolation) {
        if (!oldViolation.equals(newViolation)) {
            throw new IllegalArgumentException("Cannot merge entityIds of different violations");
        }

        var updatedIds = Stream.of(oldViolation.getEntityIds(), newViolation.getEntityIds())
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        return oldViolation.withEntityIds(updatedIds);
    }

    // Returns doctor with merged appointmentsByLocation
    private Doctor getUpdatedDoctor(Doctor oldDoc, Doctor newDoc) {
        if (!oldDoc.equals(newDoc)) {
            throw new IllegalArgumentException("Cannot merge appointmentsByLocation of different doctors");
        }

        return oldDoc.withAppointmentsByLocation(
                mergeLocationLists(
                        oldDoc.getAppointmentsByLocation(),
                        newDoc.getAppointmentsByLocation()
                ));
    }

    private List<Location> mergeLocationLists(List<Location> oldLocs, List<Location> newLocs) {
        return new ArrayList<>(
                Stream.of(oldLocs, newLocs)
                        .flatMap(List::stream)
                        .collect(Collectors.toMap(
                                Location::getLocationName,
                                location -> location,
                                this::getUpdatedLocation
                        ))
                        .values());
    }

    // Returns location with merged appointments
    private Location getUpdatedLocation(Location oldLoc, Location newLoc) {
        if (!oldLoc.equals(newLoc)) {
            throw new IllegalArgumentException("Cannot merge appointments of different locations");
        }

        var updatedList = Stream.of(oldLoc.getAppointments(), newLoc.getAppointments())
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());

        return oldLoc.withAppointments(updatedList);
    }
}

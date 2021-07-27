package com.sesame.test.converter;

import com.sesame.test.client.AppointmentApi;
import com.sesame.test.exception.ConstraintException;
import com.sesame.test.utils.ApiMother;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.sesame.test.converter.ApiModelsConverter.convertAppointmentToDoctor;
import static org.junit.jupiter.api.Assertions.*;


class ApiModelsConverterTest {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Test
    public void convertAppointmentToDoctor_validAppointment_returnsDoctor() throws ConstraintException {
        var appointmentFromApi = ApiMother.validAppointment();

        var doctor = convertAppointmentToDoctor(appointmentFromApi);
        var location = doctor.getAppointmentsByLocation().get(0);
        var appointment = location.getAppointments().get(0);
        var service = appointment.getService();

        assertEquals(appointmentFromApi.getDoctor().getFirstName(), doctor.getFirstName());
        assertEquals(appointmentFromApi.getDoctor().getLastName(), doctor.getLastName());
        assertEquals(appointmentFromApi.getId(), appointment.getAppointmentId());
        assertEquals(appointmentFromApi.getLocation().getName(), location.getLocationName());
        assertEquals(appointmentFromApi.getService().getName(), service.getServiceName());
        assertEquals(
                getDateTime(appointmentFromApi.getTime(), appointmentFromApi.getLocation().getTimeZoneCode()),
                appointment.getStartDateTime());
        assertEquals(
                Duration.ofMinutes(appointmentFromApi.getDurationInMinutes()),
                appointment.getDuration());
    }

    @Test
    public void convertAppointmentToDoctor_invalidDoctorFirstName_trows() {
        var doctorFromApi = ApiMother.validDoctor();
        doctorFromApi.setFirstName("");
        var appointmentFromApi = ApiMother.validAppointment();
        appointmentFromApi.setDoctor(doctorFromApi);

        assertThrows(appointmentFromApi, List.of("firstNameNotBlank"));
    }

    @Test
    public void convertAppointmentToDoctor_invalidDoctorLastName_trows() {
        var doctorFromApi = ApiMother.validDoctor();
        doctorFromApi.setLastName("");
        var appointmentFromApi = ApiMother.validAppointment();
        appointmentFromApi.setDoctor(doctorFromApi);

        assertThrows(appointmentFromApi, List.of("lastNameNotBlank"));
    }

    @Test
    public void convertAppointmentToDoctor_invalidDoctor_trows() {
        var doctorFromApi = ApiMother.validDoctor();
        doctorFromApi.setFirstName("");
        doctorFromApi.setLastName("");
        var appointmentFromApi = ApiMother.validAppointment();
        appointmentFromApi.setDoctor(doctorFromApi);

        assertThrows(appointmentFromApi, List.of("firstNameNotBlank", "lastNameNotBlank"));
    }

    @Test
    public void convertAppointmentToDoctor_invalidLocationName_trows() {
        var locationFromApi = ApiMother.validLocation();
        locationFromApi.setName("");
        var appointmentFromApi = ApiMother.validAppointment();
        appointmentFromApi.setLocation(locationFromApi);

        assertThrows(appointmentFromApi, List.of("locationNameNotBlank"));
    }

    @Test
    public void convertAppointmentToDoctor_invalidServiceName_trows() {
        var serviceFromApi = ApiMother.validService();
        serviceFromApi.setName("");
        var appointmentFromApi = ApiMother.validAppointment();
        appointmentFromApi.setService(serviceFromApi);

        assertThrows(appointmentFromApi, List.of("serviceNameNotBlank"));
    }

    @Test
    public void convertAppointmentToDoctor_invalidPrice_trows() {
        var serviceFromApi = ApiMother.validService();
        serviceFromApi.setPrice(0);
        var appointmentFromApi = ApiMother.validAppointment();
        appointmentFromApi.setService(serviceFromApi);

        assertThrows(appointmentFromApi, List.of("pricePositive"));
    }

    @Test
    public void convertAppointmentToDoctor_invalidId_trows() {
        var appointmentFromApi = ApiMother.validAppointment();
        appointmentFromApi.setId("");

        assertThrows(appointmentFromApi, List.of("appointmentIdNotBlank"));
    }

    @Test
    public void convertAppointmentToDoctor_invalidDateTime_throws() throws ConstraintException {
        var appointmentFromApi = ApiMother.validAppointment();
        appointmentFromApi.setTime("");

        assertThrows(appointmentFromApi, List.of("dateTimeNotNull"));
    }

    @Test
    public void convertAppointmentToDoctor_invalidDuration_throws() throws ConstraintException {
        var appointmentFromApi = ApiMother.validAppointment();
        appointmentFromApi.setDurationInMinutes(-10);

        assertThrows(appointmentFromApi, List.of("durationPositive"));
    }

    private void assertThrows(AppointmentApi appointmentApi, List<String> messages) {
        var exception = Assertions.assertThrows(ConstraintException.class, () -> {
            convertAppointmentToDoctor(appointmentApi);
        });

        assertTrue(exception.getViolations().containsAll(messages));
    }

    private ZonedDateTime getDateTime(String dateTime, String timeZoneCode) {
        var zone = ZoneId.of(timeZoneCode);
        var fmt = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).withZone(zone);
        return ZonedDateTime.parse(dateTime, fmt);
    }
}
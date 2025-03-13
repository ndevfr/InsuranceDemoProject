package fr.ndev.insurance;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ndev.insurance.dto.VehicleDTO;
import fr.ndev.insurance.enums.FuelType;
import fr.ndev.insurance.enums.Role;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.model.Vehicle;
import fr.ndev.insurance.repository.UserRepository;
import fr.ndev.insurance.repository.VehicleRepository;
import fr.ndev.insurance.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class VehicleManageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private VehicleRepository vehicleRepository;

    private String tokenClient = "";
    private String tokenAdmin = "";

    @BeforeEach
    public void setUp() {
        if(userRepository.findByEmail("test-client@gmail.com") == null) {
            User client = new User();
            client.setFirstname("Client");
            client.setLastname("User");
            client.setEmail("test-client@gmail.com");
            client.setPassword(passwordEncoder.encode("12345678"));
            client.setRole(Role.CLIENT);
            userRepository.save(client);
            this.tokenClient = getToken(client);
        } else {
            User client = userRepository.findByEmail("test-client@gmail.com");
            this.tokenClient = getToken(client);
        }

        if(userRepository.findByEmail("test-admin@gmail.com") == null) {
            User client = new User();
            client.setFirstname("Admin");
            client.setLastname("User");
            client.setEmail("test-admin@gmail.com");
            client.setPassword(passwordEncoder.encode("12345678"));
            client.setRole(Role.ADMIN);
            userRepository.save(client);
            this.tokenAdmin = getToken(client);
        } else {
            User client = userRepository.findByEmail("test-admin@gmail.com");
            this.tokenAdmin = getToken(client);
        }
    }

    @AfterEach
    public void tearDown() {
        User user = userRepository.findByEmail("test-client@gmail.com");
        List<Vehicle> vehicles = vehicleRepository.findByUser(user);
        vehicleRepository.deleteAll(vehicles);
    }

    @Test
    public void testCreateVehicle() throws Exception {
        VehicleDTO vehicle = initVehicle();
        Long id = idClient();

        // Check if the vehicle is added successfully
        for (int i = 0; i < 4; i++) {
            vehicle = initVehicle();
            mockMvc.perform(post("/api/users/" + id + "/vehicle/add")
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(vehicle)))
                    .andExpect(status().isOk());
        }

        // Check if the vehicle is not added if the user is not authenticated
        vehicle = initVehicle();
        mockMvc.perform(post("/api/users/" + id + "/vehicle/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isUnauthorized());

        // Check if the vehicle is not added if the user is not authorized
        vehicle = initVehicle();
        mockMvc.perform(post("/api/users/" + id + "/vehicle/add")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isForbidden());

        // Check if the vehicle is not added if an information is missing
        vehicle = initVehicle();
        vehicle.setBrand("");
        mockMvc.perform(post("/api/users/" + id + "/vehicle/add")
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateVehicle() throws Exception {
        VehicleDTO vehicle;
        Long id = idClient();

        for (int i = 0; i < 4; i++) {
            vehicle = initVehicle();
            mockMvc.perform(post("/api/users/" + id + "/vehicle/add")
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(vehicle)));
        }

        // Check if the vehicle is updated successfully
        vehicle = initVehicle();
        mockMvc.perform(put("/api/users/" + id + "/vehicle/update/2")
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isOk());

        // Check if the vehicle is not found
        mockMvc.perform(put("/api/users/" + id + "/vehicle/update/12")
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isNotFound());

        // Check if the vehicle is not updated if the user is not authenticated
        mockMvc.perform(put("/api/users/" + id + "/vehicle/update/2")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isUnauthorized());

        // Check if the vehicle is not updated if the user is not authorized
        mockMvc.perform(put("/api/users/" + id + "/vehicle/update/2")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isForbidden());

        // Check if the vehicle is not updated if an information is missing
        vehicle = initVehicle();
        vehicle.setBrand(null);
        mockMvc.perform(put("/api/users/" + id + "/vehicle/update/2")
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteVehicle() throws Exception {
        VehicleDTO vehicle;
        Long id = idClient();

        for (int i = 0; i < 3; i++) {
            vehicle = initVehicle();
            mockMvc.perform(post("/api/users/" + id + "/vehicle/add")
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(vehicle)));
        }

        // Check if the user is not authentified
        mockMvc.perform(delete("/api/users/" + id + "/vehicle/delete/1"))
                .andExpect(status().isUnauthorized());

        // Check if the user is not authenticated
        mockMvc.perform(delete("/api/users/" + id + "/vehicle/delete/1")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isForbidden());


        // Check if the vehicle is deleted successfully
        mockMvc.perform(delete("/api/users/" + id + "/vehicle/delete/1")
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        // Check if the vehicle is deleted successfully
        mockMvc.perform(delete("/api/users/" + id + "/vehicle/delete/2")
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        // Check if the last vehicle is not deleted
        mockMvc.perform(delete("/api/users/" + id + "/vehicle/delete/1")
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isConflict());
    }

    public String getToken(User user) {
        return jwtUtil.generateToken(user.getEmail());
    }

    public VehicleDTO initVehicle() {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setBrand("Peugeot");
        vehicle.setModel("208");
        vehicle.setFuelType(FuelType.ESSENCE);
        vehicle.setRegistrationNumber(randomRegistrationNumber());
        vehicle.setYear(2022);
        return vehicle;
    }

    public String randomRegistrationNumber() {
        String registrationNumber = "";
        for (int i = 0; i < 2; i++) {
            registrationNumber += (char) (Math.random() * 26 + 'A');
        }
        registrationNumber += "-";
        for (int i = 0; i < 3; i++) {
            registrationNumber += (char) (Math.random() * 26 + 'A');
        }
        registrationNumber += "-";
        for (int i = 0; i < 2; i++) {
            registrationNumber += (char) (Math.random() * 26 + 'A');
        }
        return registrationNumber;
    }

    public Long idClient() {
        return userRepository.findByEmail("test-client@gmail.com").getId();
    }

}
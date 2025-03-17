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
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class VehicleControllerTest {

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

    @BeforeEach
    public void setUp() {
        if(userRepository.findFirstByEmail("test-client@gmail.com") == null) {
            User client = new User();
            client.setFirstname("Client");
            client.setLastname("User");
            client.setEmail("test-client@gmail.com");
            client.setPassword(passwordEncoder.encode("12345678"));
            client.setRole(Role.CLIENT);
            userRepository.save(client);
            this.tokenClient = getToken(client);
        } else {
            User client = userRepository.findFirstByEmail("test-client@gmail.com");
            this.tokenClient = getToken(client);
        }
    }

    @AfterEach
    public void tearDown() {
        User user = userRepository.findFirstByEmail("test-client@gmail.com");
        List<Vehicle> vehicles = vehicleRepository.findByUser(user);
        vehicleRepository.deleteAll(vehicles);
    }

    @Test
    public void testCreateVehicle() throws Exception {
        VehicleDTO vehicle = initVehicle();

        // Check if the vehicle is added successfully
        for (int i = 0; i < 4; i++) {
            vehicle = initVehicle();
            mockMvc.perform(post("/api/user/vehicles")
                    .with(csrf())
                    .header("Authorization", "Bearer " + tokenClient)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(vehicle)))
                    .andExpect(status().isOk());
        }

        // Check if the vehicle is not added if the user is not authenticated
        mockMvc.perform(post("/api/user/vehicles")
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isUnauthorized());

        // Check if the vehicle is not added if an information is missing
        vehicle.setBrand("");
        mockMvc.perform(post("/api/user/vehicles")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateVehicle() throws Exception {
        VehicleDTO vehicle;
        // Create phone
        for (int i = 0; i < 4; i++) {
            vehicle = initVehicle();
            mockMvc.perform(post("/api/user/vehicles")
                    .with(csrf())
                    .header("Authorization", "Bearer " + tokenClient)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(vehicle)));
        }

        // Check if the vehicle is updated successfully
        vehicle = initVehicle();
        mockMvc.perform(put("/api/user/vehicles/2")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isOk());

        // Check if the vehicle is not found
        mockMvc.perform(put("/api/user/vehicles/12")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isNotFound());

        // Check if the vehicle is not updated if the user is not authenticated
        mockMvc.perform(put("/api/user/vehicles/2")
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isUnauthorized());

        // Check if the vehicle is not updated if an information is missing
        vehicle = initVehicle();
        vehicle.setBrand(null);
        mockMvc.perform(put("/api/user/vehicles/2")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteVehicle() throws Exception {
        VehicleDTO vehicle;

        // Create phones
        for (int i = 0; i < 3; i++) {
            vehicle = initVehicle();
            mockMvc.perform(post("/api/user/vehicles")
                    .with(csrf())
                    .header("Authorization", "Bearer " + tokenClient)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(vehicle)));
        }


        // Check if the vehicle is deleted successfully
        mockMvc.perform(delete("/api/user/vehicles/1")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the vehicle is deleted successfully
        mockMvc.perform(delete("/api/user/vehicles/2")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());
    }

    public String getToken(User user) {
        return jwtUtil.generateToken(user.getEmail());
    }

    public VehicleDTO initVehicle() {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setBrand("Peugeot");
        vehicle.setModel("208");
        vehicle.setFuelType(FuelType.HYBRID);
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

}
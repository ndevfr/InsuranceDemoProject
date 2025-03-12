package fr.ndev.insurance;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ndev.insurance.dto.AddressDTO;
import fr.ndev.insurance.dto.PhoneDTO;
import fr.ndev.insurance.dto.VehicleDTO;
import fr.ndev.insurance.enums.Role;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.repository.UserRepository;
import fr.ndev.insurance.repository.VehicleRepository;
import fr.ndev.insurance.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

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
    private VehicleRepository vehicleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private String tokenAdmin = "";
    private String tokenClient = "";
    private String tokenAgent = "";

    @BeforeEach
    public void setUp() {
        // Clear the database before each test
        // userRepository.deleteAll();

        // Create an admin user
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("test-admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("12345678"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
        this.tokenAdmin = getToken(admin);

        // Create a client user
        User client = new User();
        client.setFirstName("Client");
        client.setLastName("User");
        client.setEmail("test-client@gmail.com");
        client.setPassword(passwordEncoder.encode("12345678"));
        client.setRole(Role.CLIENT);
        userRepository.save(client);
        this.tokenClient = getToken(client);

        // Create a agent user
        User agent = new User();
        agent.setFirstName("Agent");
        agent.setLastName("User");
        agent.setEmail("test-agent@gmail.com");
        agent.setPassword(passwordEncoder.encode("12345678"));
        agent.setRole(Role.CLIENT);
        userRepository.save(client);
        this.tokenAgent = getToken(agent);
    }

    @Test
    public void testCreateVehicle() throws Exception {
        VehicleDTO vehicle = initVehicle();

        // Check if the vehicle is added successfully
        for (int i = 0; i < 4; i++) {
            vehicle = initVehicle();
            mockMvc.perform(post("/api/user/vehicle/add")
                            .header("Authorization", "Bearer " + tokenClient)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(vehicle)))
                    .andExpect(status().isOk());
        }

        // Check if the vehicle is not added if the user is not authenticated
        mockMvc.perform(post("/api/user/vehicle/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isUnauthorized());

        // Check if the vehicle is not added if an information is missing
        vehicle.setBrand("");
        mockMvc.perform(post("/api/user/vehicle/add")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateVehicle() throws Exception {
        VehicleDTO vehicle = initVehicle();

        // Create phone
        for (int i = 0; i < 4; i++) {
            vehicle = initVehicle();
            mockMvc.perform(post("/api/user/vehicle/add")
                            .header("Authorization", "Bearer " + tokenClient)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(vehicle)));
        }

        // Check if the vehicle is updated successfully
        vehicle = initVehicle();
        mockMvc.perform(put("/api/user/vehicle/update/2")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isOk());

        // Check if the vehicle is not found
        mockMvc.perform(put("/api/user/vehicle/update/12")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isNotFound());

        // Check if the vehicle is not updated if the user is not authenticated
        mockMvc.perform(put("/api/user/vehicle/update/2")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isUnauthorized());

        // Check if the vehicle is not updated if an information is missing
        vehicle = initVehicle();
        vehicle.setBrand(null);
        mockMvc.perform(put("/api/user/vehicle/update/2")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(vehicle)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteVehicle() throws Exception {
        VehicleDTO vehicle = initVehicle();

        // Create phones
        for (int i = 0; i < 3; i++) {
            vehicle = initVehicle();
            mockMvc.perform(post("/api/user/vehicle/add")
                    .header("Authorization", "Bearer " + tokenClient)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(vehicle)));
        }


        // Check if the vehicle is deleted successfully
        mockMvc.perform(delete("/api/user/vehicle/delete/1")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the vehicle is deleted successfully
        mockMvc.perform(delete("/api/user/vehicle/delete/2")
                        .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the last vehicle is not deleted
        mockMvc.perform(delete("/api/user/vehicle/delete/1")
                        .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isConflict());
    }

    public String getToken(User user) {
        return jwtUtil.generateToken(user.getEmail());
    }

    public VehicleDTO initVehicle() {
        VehicleDTO vehicle = new VehicleDTO();
        vehicle.setBrand("Peugeot");
        vehicle.setModel("208");

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
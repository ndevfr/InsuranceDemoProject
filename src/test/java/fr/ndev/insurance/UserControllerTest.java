package fr.ndev.insurance;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ndev.insurance.dto.AddressDTO;
import fr.ndev.insurance.dto.PhoneDTO;
import fr.ndev.insurance.dto.ProfileRequest;
import fr.ndev.insurance.enums.Role;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import fr.ndev.insurance.security.JwtUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

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

    private String tokenClient = "";

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
    }

    @AfterEach
    public void tearDown() {
        User user = userRepository.findByEmail("test-client@gmail.com");
        user.clearAddresses();
        user.clearPhones();
        userRepository.save(user);
    }

    @Test
    public void testCreatePhone() throws Exception {
        PhoneDTO phone = initPhone();

        // Check if the phone is added successfully
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(post("/api/user/phones")
                    .header("Authorization", "Bearer " + tokenClient)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(phone)))
                    .andExpect(status().isOk());
        }

        // Check if the phone is not added if the user is not authentified
        mockMvc.perform(post("/api/user/phones")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isUnauthorized());

        // Check if the phone is not added if an information is missing
        phone.setPhoneNumber(null);
        mockMvc.perform(post("/api/user/phones")
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdatePhone() throws Exception {
        PhoneDTO phone = initPhone();

        // Create phone
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(post("/api/user/phones")
                    .header("Authorization", "Bearer " + tokenClient)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(phone)));
        }

        // Check if the phone is updated successfully
        mockMvc.perform(put("/api/user/phones/2")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isOk());

        // Check if the phone is not found
        mockMvc.perform(put("/api/user/phones/12")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isNotFound());

        // Check if the phone is not updated if the user is not authentified
        mockMvc.perform(put("/api/user/phones/2")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isUnauthorized());

        // Check if the phone is not updated if an information is missing
        phone.setPhoneNumber(null);
        mockMvc.perform(put("/api/user/phones/2")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteAndMainPhone() throws Exception {
        PhoneDTO phone = initPhone();

        // Create phones
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post("/api/user/phones")
                    .header("Authorization", "Bearer " + tokenClient)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(phone)));
        }

        // Check with no authentication
        mockMvc.perform(put("/api/user/phones/1"))
                .andExpect(status().isUnauthorized());

        // Check if the phone is not deleted if it is the main phone
        mockMvc.perform(delete("/api/user/phones/1")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isConflict());

        // Change the main phone with the same phone
        mockMvc.perform(put("/api/user/phones/2/main")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the address is deleted successfully
        mockMvc.perform(delete("/api/user/phones/1")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the phone is deleted successfully
        mockMvc.perform(delete("/api/user/phones/2")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the last phone is not deleted
        mockMvc.perform(delete("/api/user/phones/1")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isConflict());
    }

    @Test
    public void testAddAddress() throws Exception {
        AddressDTO address = initAddress();

        // Check if the address is added successfully
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(post("/api/user/addresses")
                            .header("Authorization", "Bearer " + tokenClient)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(address)))
                    .andExpect(status().isOk());
        }

        // Check if the address is not added if the user is not authenticated
        mockMvc.perform(post("/api/user/address")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isUnauthorized());

        // Check if the address is not added if an information is missing
        address.setCity(null);
        mockMvc.perform(post("/api/user/addresses")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateAddress() throws Exception {
        AddressDTO address = initAddress();

        // Create addresses
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(post("/api/user/addresses")
                    .header("Authorization", "Bearer " + tokenClient)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(address)));
        }

        // Check if the address is updated successfully
        mockMvc.perform(put("/api/user/addresses/2")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isOk());

        // Check if the address is not found
        mockMvc.perform(put("/api/user/addresses/12")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isNotFound());

        // Check if the address is not updated if the user is not authenticated
        mockMvc.perform(put("/api/user/addresses/2")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isUnauthorized());

        // Check if the address is not updated if an information is missing
        address.setCity(null);
        mockMvc.perform(put("/api/user/addresses/2")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteAndMainAddress() throws Exception {
        AddressDTO address = initAddress();

        // Create addresses
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post("/api/user/addresses")
                    .header("Authorization", "Bearer " + tokenClient)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(address)));
        }

        // Check with no authentication
        mockMvc.perform(put("/api/user/phones/1"))
                .andExpect(status().isUnauthorized());

        // Check if the address is not deleted if it is the main address
        mockMvc.perform(delete("/api/user/addresses/1")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isConflict());

        // Change the main address with the same address
        mockMvc.perform(put("/api/user/addresses/2/main")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the address is deleted successfully
        mockMvc.perform(delete("/api/user/addresses/1")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the address is deleted successfully
        mockMvc.perform(delete("/api/user/addresses/2")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the last address is not deleted
        mockMvc.perform(delete("/api/user/addresses/1")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isConflict());


    }

    @Test
    public void testChangeFirstName() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setFirstname("John");

        // Create firstname with valid text
        mockMvc.perform(put("/api/user")
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isOk());

        profileRequest.setFirstname("");

        // Create firstname with invalid text
        mockMvc.perform(put("/api/user")
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangeLastName() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setLastname("Lock");

        // Create lastname with valid text
        mockMvc.perform(put("/api/user")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(profileRequest)))
                        .andExpect(status().isOk());

        profileRequest.setLastname("");

        // Create lastname with invalid text
        mockMvc.perform(put("/api/user")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(profileRequest)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangeEmail() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("test-client@gmail.com");

        // Create email with invalid text
        mockMvc.perform(put("/api/user")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(profileRequest)))
                        .andExpect(status().isOk());

        profileRequest.setEmail("");

        // Create email with valid text
        mockMvc.perform(put("/api/user")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(profileRequest)))
                        .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangePassword() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setPassword("12345678");

        // Create password with valid text
        mockMvc.perform(put("/api/user")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(profileRequest)))
                        .andExpect(status().isOk());

        profileRequest.setPassword("");

        // Create password with invalid text
        mockMvc.perform(put("/api/user")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(profileRequest)))
                        .andExpect(status().isBadRequest());
    }

    public String getToken(User user) {
        return jwtUtil.generateToken(user.getEmail());
    }

    public AddressDTO initAddress() {
        AddressDTO address = new AddressDTO();
        address.setStreet("12 Rue de la Paix");
        address.setCity("Paris");
        address.setZipCode("75000");
        address.setCountry("France");
        return address;
    }

    public PhoneDTO initPhone() {
        PhoneDTO phone = new PhoneDTO();
        phone.setPhoneNumber("0123456789");
        return phone;
    }

}
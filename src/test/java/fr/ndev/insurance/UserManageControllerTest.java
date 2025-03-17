package fr.ndev.insurance;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ndev.insurance.dto.AddressDTO;
import fr.ndev.insurance.dto.PhoneDTO;
import fr.ndev.insurance.dto.ProfileRequest;
import fr.ndev.insurance.enums.Role;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.repository.UserRepository;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserManageControllerTest {

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

    private String tokenAdmin = "";
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

        if(userRepository.findFirstByEmail("test-admin@gmail.com") == null) {
            User client = new User();
            client.setFirstname("Admin");
            client.setLastname("User");
            client.setEmail("test-admin@gmail.com");
            client.setPassword(passwordEncoder.encode("12345678"));
            client.setRole(Role.ADMIN);
            userRepository.save(client);
            this.tokenAdmin = getToken(client);
        } else {
            User client = userRepository.findFirstByEmail("test-admin@gmail.com");
            this.tokenAdmin = getToken(client);
        }
    }

    @AfterEach
    public void tearDown() {
        User user = userRepository.findFirstByEmail("test-client@gmail.com");
        user.clearAddresses();
        user.clearPhones();
        userRepository.save(user);
    }

    @Test
    public void testCreatePhone() throws Exception {
        PhoneDTO phone = initPhone();
        Long id = idClient();

        // Check if the phone is added successfully
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(post("/api/agent/users/" + id + "/phones")
                    .with(csrf())
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(phone)))
                    .andExpect(status().isOk());
        }

        // Check if the phone is not added if the user is not authenticated
        mockMvc.perform(post("/api/agent/users/" + id + "/phones")
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isUnauthorized());

        // Check if the phone is not added if the user is not authorized
        mockMvc.perform(post("/api/agent/users/" + id + "/phones")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isForbidden());

        // Check if the phone is not added if an information is missing
        phone.setPhoneNumber(null);
        mockMvc.perform(post("/api/agent/users/" + id + "/phones")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdatePhone() throws Exception {
        PhoneDTO phone = initPhone();
        Long id = idClient();

        // Create phone
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(post("/api/agent/users/" + id + "/phones")
                    .with(csrf())
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(phone)));
        }

        // Check if the phone is updated successfully
        mockMvc.perform(put("/api/agent/users/" + id + "/phones/2")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isOk());

        // Check if the phone is not found
        mockMvc.perform(put("/api/agent/users/" + id + "/phones/12")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isNotFound());

        // Check if the phone is not updated if the user is not authenticated
        mockMvc.perform(put("/api/agent/users/" + id + "/phones/2")
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isUnauthorized());

        // Check if the phone is not updated if the user is not authorized
        mockMvc.perform(put("/api/agent/users/" + id + "/phones/2")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isForbidden());

        // Check if the phone is not updated if an information is missing
        phone.setPhoneNumber(null);
        mockMvc.perform(put("/api/agent/users/" + id + "/phones/2")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteAndMainPhone() throws Exception {
        PhoneDTO phone = initPhone();
        int id = idClient().intValue();

        // Create phones
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post("/api/agent/users/" + id + "/phones")
                    .with(csrf())
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(phone)));
        }

        // Check when the user is not authenticated
        mockMvc.perform(delete("/api/agent/users/" + id + "/phones/1")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isForbidden());

        // Check when the user is not authentified
        mockMvc.perform(delete("/api/agent/users/" + id + "/phones/1")
                .with(csrf()))
                .andExpect(status().isUnauthorized());


        // Check if the phone is not deleted if it is the main phone
        mockMvc.perform(delete("/api/agent/users/" + id + "/phones/1")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isConflict());

        // Change the main phone with the same phone
        mockMvc.perform(put("/api/agent/users/" + id + "/phones/2/main")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        // Check if the address is deleted successfully
        mockMvc.perform(delete("/api/agent/users/" + id + "/phones/1")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        // Check if the phone is deleted successfully
        mockMvc.perform(delete("/api/agent/users/" + id + "/phones/2")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        // Check if the last phone is not deleted
        mockMvc.perform(delete("/api/agent/users/" + id + "/phones/1")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isConflict());
    }

    @Test
    public void testAddAddress() throws Exception {
        AddressDTO address = initAddress();
        Long id = idClient();

        // Check if the address is added successfully
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(post("/api/agent/users/" + id + "/addresses")
                    .with(csrf())
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(address)))
                    .andExpect(status().isOk());
        }

        // Check if the address is not added if the user is not authenticated
        mockMvc.perform(post("/api/agent/users/" + id + "/addresses")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isForbidden());

        // Check if the address is not added if the user is not authentified
        mockMvc.perform(post("/api/agent/users/" + id + "/addresses")
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isUnauthorized());

        // Check if the address is not added if an information is missing
        address.setCity(null);
        mockMvc.perform(post("/api/agent/users/" + id + "/addresses")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateAddress() throws Exception {
        AddressDTO address = initAddress();
        Long id = idClient();

        // Create addresses
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(post("/api/agent/users/" + id + "/addresses")
                    .with(csrf())
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(address)));
        }

        // Check if the address is updated successfully
        mockMvc.perform(put("/api/agent/users/" + id + "/addresses/2")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isOk());

        // Check if the address is not found
        mockMvc.perform(put("/api/agent/users/" + id + "/addresses/12")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isNotFound());

        // Check if the address is not updated if the user is not authentified
        mockMvc.perform(put("/api/agent/users/" + id + "/addresses/2")
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isUnauthorized());

        // Check if the address is not updated if the user is not authenticated
        mockMvc.perform(put("/api/agent/users/" + id + "/addresses/2")
                .with(csrf())
                .contentType("application/json")
                .header("Authorization", "Bearer " + tokenClient)
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isForbidden());

        // Check if the address is not updated if an information is missing
        address.setCity(null);
        mockMvc.perform(put("/api/agent/users/" + id + "/addresses/2")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteAndMainAddress() throws Exception {
        AddressDTO address = initAddress();
        Long id = idClient();

        // Create addresses
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post("/api/agent/users/" + id + "/addresses")
                    .with(csrf())
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(address)));
        }

        // Check if the user is not authentified
        mockMvc.perform(delete("/api/agent/users/" + id + "/addresses/1")
                .with(csrf()))
                .andExpect(status().isUnauthorized());

        // Check if the user is not authenticated
        mockMvc.perform(delete("/api/agent/users/" + id + "/addresses/1")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isForbidden());

        // Change the main address with the same address
        mockMvc.perform(put("/api/agent/users/" + id + "/addresses/2/main")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        // Check if the address is deleted successfully
        mockMvc.perform(delete("/api/agent/users/" + id + "/addresses/1")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        // Check if the address is deleted successfully
        mockMvc.perform(delete("/api/agent/users/" + id + "/addresses/2")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        // Check if the last address is not deleted
        mockMvc.perform(delete("/api/agent/users/" + id + "/addresses/1")
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isConflict());


    }

    @Test
    public void testChangeFirstName() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setFirstname("John");
        Long id = idClient();

        // Create firstname with valid text
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isOk());

        // Check if unauthentified
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isUnauthorized());

        //Check if unauthenticated
        // Create firstname with valid text
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isForbidden());



        profileRequest.setFirstname("");

        // Create firstname with invalid text
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangeLastName() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setLastname("Lock");
        Long id = idClient();

        // Create lastname with valid text
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isOk());

        // Check if unauthentified
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isUnauthorized());

        //Check if unauthenticated
        // Create firstname with valid text
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isForbidden());

        profileRequest.setLastname("");

        // Create lastname with invalid text
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangeEmail() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setEmail("test-client@gmail.com");
        Long id = idClient();

        // Create email with invalid text
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isOk());

        // Check if unauthentified
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isUnauthorized());

        //Check if unauthenticated
        // Create firstname with valid text
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isForbidden());

        profileRequest.setEmail("");

        // Create email with valid text
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangePassword() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setPassword("12345678");
        Long id = idClient();

        // Create password with valid text
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isOk());

        // Check if unauthentified
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isUnauthorized());

        //Check if unauthenticated
        // Create firstname with valid text
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isForbidden());

        profileRequest.setPassword("");

        // Create password with invalid text
        mockMvc.perform(put("/api/agent/users/" + id)
                .with(csrf())
                .header("Authorization", "Bearer " + tokenAdmin)
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

    public Long idClient() {
        return userRepository.findFirstByEmail("test-client@gmail.com").getId();
    }

}
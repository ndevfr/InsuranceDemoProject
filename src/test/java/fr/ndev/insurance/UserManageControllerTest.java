package fr.ndev.insurance;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ndev.insurance.dto.AddressDTO;
import fr.ndev.insurance.dto.PhoneDTO;
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
            mockMvc.perform(post("/api/users/" + id + "/phone/add")
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(phone)))
                    .andExpect(status().isOk());
        }

        // Check if the phone is not added if the user is not authenticated
        mockMvc.perform(post("/api/users/" + id + "/phone/add")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isUnauthorized());

        // Check if the phone is not added if the user is not authorized
        mockMvc.perform(post("/api/users/" + id + "/phone/add")
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isForbidden());

        // Check if the phone is not added if an information is missing
        phone.setPhoneNumber(null);
        mockMvc.perform(post("/api/users/" + id + "/phone/add")
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
            mockMvc.perform(post("/api/users/" + id + "/phone/add")
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(phone)));
        }

        // Check if the phone is updated successfully
        mockMvc.perform(put("/api/users/" + id + "/phone/update/2")
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isOk());

        // Check if the phone is not found
        mockMvc.perform(put("/api/users/" + id + "/phone/update/12")
                .header("Authorization", "Bearer " + tokenAdmin)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isNotFound());

        // Check if the phone is not updated if the user is not authenticated
        mockMvc.perform(put("/api/users/" + id + "/phone/update/2")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isUnauthorized());

        // Check if the phone is not updated if the user is not authorized
        mockMvc.perform(put("/api/users/" + id + "/phone/update/2")
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isForbidden());

        // Check if the phone is not updated if an information is missing
        phone.setPhoneNumber(null);
        mockMvc.perform(put("/api/users/" + id + "/phone/update/2")
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
            mockMvc.perform(post("/api/users/" + id + "/phone/add")
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(phone)));
        }

        // Check when the user is not authenticated
        mockMvc.perform(delete("/api/users/" + id + "/phone/delete/1")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isForbidden());

        // Check when the user is not authentified
        mockMvc.perform(delete("/api/users/" + id + "/phone/delete/1"))
                .andExpect(status().isUnauthorized());


        // Check if the phone is not deleted if it is the main phone
        mockMvc.perform(delete("/api/users/" + id + "/phone/delete/1")
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isConflict());

        // Change the main phone with the same phone
        mockMvc.perform(put("/api/users/" + id + "/phone/main/2")
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        // Check if the address is deleted successfully
        mockMvc.perform(delete("/api/users/" + id + "/phone/delete/1")
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        // Check if the phone is deleted successfully
        mockMvc.perform(delete("/api/users/" + id + "/phone/delete/2")
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        // Check if the last phone is not deleted
        mockMvc.perform(delete("/api/users/" + id + "/phone/delete/1")
                .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isConflict());
    }

    @Test
    public void testAddAddress() throws Exception {
        AddressDTO address = initAddress();
        Long id = idClient();

        // Check if the address is added successfully
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(post("/api/users/" + id + "/address/add")
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(address)))
                    .andExpect(status().isOk());
        }

        // Check if the address is not added if the user is not authenticated
        mockMvc.perform(post("/api/users/" + id + "/address/add")
                .header("Authorization", "Bearer " + tokenClient)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isForbidden());

        // Check if the address is not added if the user is not authentified
        mockMvc.perform(post("/api/users/" + id + "/address/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isUnauthorized());

        // Check if the address is not added if an information is missing
        address.setCity(null);
        mockMvc.perform(post("/api/users/" + id + "/address/add")
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
            mockMvc.perform(post("/api/users/" + id + "/address/add")
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(address)));
        }

        // Check if the address is updated successfully
        mockMvc.perform(put("/api/users/" + id + "/address/update/2")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(address)))
                        .andExpect(status().isOk());

        // Check if the address is not found
        mockMvc.perform(put("/api/users/" + id + "/address/update/12")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isNotFound());

        // Check if the address is not updated if the user is not authentified
        mockMvc.perform(put("/api/users/" + id + "/address/update/2")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isUnauthorized());

        // Check if the address is not updated if the user is not authenticated
        mockMvc.perform(put("/api/users/" + id + "/address/update/2")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + tokenClient)
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isForbidden());

        // Check if the address is not updated if an information is missing
        address.setCity(null);
        mockMvc.perform(put("/api/users/" + id + "/address/update/2")
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
            mockMvc.perform(post("/api/users/" + id + "/address/add")
                    .header("Authorization", "Bearer " + tokenAdmin)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(address)));
        }

        // Check if the user is not authentified
        mockMvc.perform(delete("/api/users/" + id + "/address/delete/1"))
                .andExpect(status().isUnauthorized());

        // Check if the user is not authenticated
        mockMvc.perform(delete("/api/users/" + id + "/address/delete/1")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isForbidden());

        // Change the main address with the same address
        mockMvc.perform(put("/api/users/" + id + "/address/main/2")
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        // Check if the address is deleted successfully
        mockMvc.perform(delete("/api/users/" + id + "/address/delete/1")
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        // Check if the address is deleted successfully
        mockMvc.perform(delete("/api/users/" + id + "/address/delete/2")
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());

        // Check if the last address is not deleted
        mockMvc.perform(delete("/api/users/" + id + "/address/delete/1")
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isConflict());


    }

    @Test
    public void testChangeFirstname() throws Exception {
        Long id = idClient();

        // Create firstname with valid text
        mockMvc.perform(put("/api/users/" + id + "/firstname/update")
                .header("Authorization", "Bearer " + tokenAdmin)
                .param("firstname", "John"))
                .andExpect(status().isOk());

        // Create firstname for unauthentified
        mockMvc.perform(put("/api/users/" + id + "/firstname/update")
                .param("firstname", "John"))
                .andExpect(status().isUnauthorized());

        // Create firstname for unauthenticated
        mockMvc.perform(put("/api/users/" + id + "/firstname/update")
                .header("Authorization", "Bearer " + tokenClient)
                .param("firstname", "John"))
                .andExpect(status().isForbidden());

        // Create firstname with invalid text
        mockMvc.perform(put("/api/users/" + id + "/firstname/update")
                .header("Authorization", "Bearer " + tokenAdmin)
                .param("firstname", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangeLastname() throws Exception {
        Long id = idClient();

        // Create lastname with valid text
        mockMvc.perform(put("/api/users/" + id + "/lastname/update")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .param("lastname", "John"))
                .andExpect(status().isOk());

        // Create lastname for unauthentified
        mockMvc.perform(put("/api/users/" + id + "/lastname/update")
                        .param("lastname", "John"))
                .andExpect(status().isUnauthorized());

        // Create lastname for unauthenticated
        mockMvc.perform(put("/api/users/" + id + "/lastname/update")
                        .header("Authorization", "Bearer " + tokenClient)
                        .param("lastname", "John"))
                .andExpect(status().isForbidden());

        // Create lastname with invalid text
        mockMvc.perform(put("/api/users/" + id + "/lastname/update")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .param("lastname", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangeEmail() throws Exception {
        Long id = idClient();

        // Create email for unauthentified
        mockMvc.perform(put("/api/users/" + id + "/email/update")
                .param("email", "test-client@gmail.com"))
                .andExpect(status().isUnauthorized());

        // Create email for unauthenticated
        mockMvc.perform(put("/api/users/" + id + "/email/update")
                .header("Authorization", "Bearer " + tokenClient)
                .param("email", "test-client@gmail.com"))
                .andExpect(status().isForbidden());

        // Create email with invalid text
        mockMvc.perform(put("/api/users/" + id + "/lastname/update")
                .header("Authorization", "Bearer " + tokenAdmin)
                .param("email", ""))
                .andExpect(status().isBadRequest());

        // Create email with valid text
        mockMvc.perform(put("/api/users/" + id + "/email/update")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .param("email", "test-client@gmail.com"))
                .andExpect(status().isOk());
    }

    @Test
    public void testPassword() throws Exception {
        Long id = idClient();

        // Create password for unauthentified
        mockMvc.perform(put("/api/users/" + id + "/password/update")
                        .param("password", "12345678"))
                .andExpect(status().isUnauthorized());

        // Create password for unauthenticated
        mockMvc.perform(put("/api/users/" + id + "/password/update")
                        .with(user("admin").password("pass").roles("CLIENT"))
                        .param("password", "12345678"))
                .andExpect(status().isForbidden());

        // Create password with invalid text
        mockMvc.perform(put("/api/users/" + id + "/password/update")
                        .with(user("admin").password("pass").roles("ADMIN"))
                        .param("password", ""))
                .andExpect(status().isBadRequest());

        // Create password with valid text
        mockMvc.perform(put("/api/users/" + id + "/password/update")
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .param("password", "12345678"))
                .andExpect(status().isOk());
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
        return userRepository.findByEmail("test-client@gmail.com").getId();
    }

}
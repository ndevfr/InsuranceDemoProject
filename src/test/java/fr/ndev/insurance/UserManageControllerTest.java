package fr.ndev.insurance;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ndev.insurance.dto.AddressDTO;
import fr.ndev.insurance.dto.PhoneDTO;
import fr.ndev.insurance.enums.Role;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.repository.UserRepository;
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
    private String agentClient = "";

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
        agent.setRole(Role.AGENT);
        userRepository.save(client);
        this.agentClient = getToken(agent);
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

        // Create phone
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(post("/api/user/phone/add")
                            .header("Authorization", "Bearer " + tokenClient)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(phone)));
        }

        // Check if the phone is updated successfully
        mockMvc.perform(put("/api/user/phone/update/2")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isOk());

        // Check if the phone is not found
        mockMvc.perform(put("/api/user/phone/update/12")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isNotFound());

        // Check if the phone is not updated if the user is not authenticated
        mockMvc.perform(put("/api/user/phone/update/2")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(phone)))
                .andExpect(status().isUnauthorized());

        // Check if the phone is not updated if an information is missing
        phone.setPhoneNumber(null);
        mockMvc.perform(put("/api/user/address/update/2")
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
            mockMvc.perform(post("/api/user/phone/add")
                    .header("Authorization", "Bearer " + tokenClient)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(phone)));
        }

        // Check if the phone is not deleted if it is the main phone
        mockMvc.perform(delete("/api/user/phone/delete/1")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isConflict());

        // Change the main phone with the same phone
        mockMvc.perform(put("/api/user/phone/main/2")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the address is deleted successfully
        mockMvc.perform(delete("/api/user/phone/delete/1")
                .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the phone is deleted successfully
        mockMvc.perform(delete("/api/user/phone/delete/2")
                        .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the last phone is not deleted
        mockMvc.perform(delete("/api/user/phone/delete/1")
                        .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isConflict());
    }

    @Test
    public void testAddAddress() throws Exception {
        AddressDTO address = initAddress();

        // Check if the address is added successfully
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(post("/api/user/address/add")
                            .header("Authorization", "Bearer " + tokenClient)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(address)))
                    .andExpect(status().isOk());
        }

        // Check if the address is not added if the user is not authenticated
        mockMvc.perform(post("/api/user/address/add")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isUnauthorized());

        // Check if the address is not added if an information is missing
        address.setCity(null);
        mockMvc.perform(post("/api/user/address/add")
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
            mockMvc.perform(post("/api/user/address/add")
                    .header("Authorization", "Bearer " + tokenClient)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(address)));
        }

        // Check if the address is updated successfully
        mockMvc.perform(put("/api/user/address/update/2")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isOk());

        // Check if the address is not found
        mockMvc.perform(put("/api/user/address/update/12")
                        .header("Authorization", "Bearer " + tokenClient)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isNotFound());

        // Check if the address is not updated if the user is not authenticated
        mockMvc.perform(put("/api/user/address/update/2")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(address)))
                .andExpect(status().isUnauthorized());

        // Check if the address is not updated if an information is missing
        address.setCity(null);
        mockMvc.perform(put("/api/user/address/update/2")
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
            mockMvc.perform(post("/api/user/address/add")
                    .header("Authorization", "Bearer " + tokenClient)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(address)));
        }

        // Check if the address is not deleted if it is the main address
        mockMvc.perform(delete("/api/user/address/delete/1")
                        .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isConflict());

        // Change the main address with the same address
        mockMvc.perform(put("/api/user/address/main/2")
                        .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the address is deleted successfully
        mockMvc.perform(delete("/api/user/address/delete/1")
                        .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the address is deleted successfully
        mockMvc.perform(delete("/api/user/address/delete/2")
                        .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isOk());

        // Check if the last address is not deleted
        mockMvc.perform(delete("/api/user/address/delete/1")
                        .header("Authorization", "Bearer " + tokenClient))
                .andExpect(status().isConflict());


    }

    @Test
    public void testChangeFirstName() throws Exception {
        // Create firstname with valid text
        mockMvc.perform(put("/api/user/firstname/update")
                .header("Authorization", "Bearer " + tokenClient)
                .param("firstname", "John"))
                .andExpect(status().isOk());

        // Create firstname with invalid text
        mockMvc.perform(put("/api/user/firstname/update")
                .header("Authorization", "Bearer " + tokenClient)
                .param("firstname", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangeLastName() throws Exception {
        // Create lastname with valid text
        mockMvc.perform(put("/api/user/lastname/update")
                        .header("Authorization", "Bearer " + tokenClient)
                        .param("lastname", "John"))
                .andExpect(status().isOk());

        // Create lastname with invalid text
        mockMvc.perform(put("/api/user/lastname/update")
                        .header("Authorization", "Bearer " + tokenClient)
                        .param("lastname", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangeEmail() throws Exception {
        // Create email with invalid text
        mockMvc.perform(put("/api/user/email/update")
                        .header("Authorization", "Bearer " + tokenClient)
                        .param("email", "fdfds"))
                .andExpect(status().isBadRequest());

        // Create email with valid text
        mockMvc.perform(put("/api/user/email/update")
                        .header("Authorization", "Bearer " + tokenClient)
                        .param("email", "nicolas@test.fr"))
                .andExpect(status().isOk());
    }

    @Test
    public void testChangePassword() throws Exception {
        // Create password with valid text
        mockMvc.perform(put("/api/user/password/update")
                        .header("Authorization", "Bearer " + tokenClient)
                        .param("password", "1234"))
                .andExpect(status().isOk());

        // Create password with invalid text
        mockMvc.perform(put("/api/user/password/update")
                        .header("Authorization", "Bearer " + tokenClient)
                        .param("password", ""))
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
        return userRepository.findByEmail("test-admin@gmail.com").getId();
    }

}
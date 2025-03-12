package fr.ndev.insurance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ndev.insurance.dto.LoginRequest;
import fr.ndev.insurance.enums.Role;
import fr.ndev.insurance.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import fr.ndev.insurance.repository.UserRepository;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        // Clear the database before each test
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUserSuccessfully() throws Exception {
        User user = new User();
        user.setFirstName("Marc");
        user.setLastName("Scout");
        user.setEmail("marc.scout@lumen.com");
        user.setPassword(passwordEncoder.encode("12345678"));
        user.setRole(Role.CLIENT);

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"status\":\"201 CREATED\",\"message\":\"User marc.scout@lumen.com created successfully\"}"));
    }

    @Test
    public void testCreateUserAlreadyExists() throws Exception {
        User user = new User();
        user.setFirstName("Marc");
        user.setLastName("Scout");
        user.setEmail("marc.scout@lumen.com");
        user.setPassword(passwordEncoder.encode("12345678"));
        user.setRole(Role.CLIENT);

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"status\":\"201 CREATED\",\"message\":\"User marc.scout@lumen.com created successfully\"}"));

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict())
                .andExpect(content().json("{\"status\":\"409 CONFLICT\",\"message\":\"Email marc.scout@lumen.com already exists\"}"));
    }

    @Test
    public void testCreateUserInvalidInput() throws Exception {
        User user = new User();
        user.setFirstName("Marc");
        user.setLastName("Scout");
        user.setEmail("");
        user.setPassword(passwordEncoder.encode("12345678"));
        user.setRole(Role.CLIENT);

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"status\":\"400 BAD_REQUEST\",\"errors\":[\"email must not be blank\"]}"));

        user.setEmail("marc.scout@lumen.com");
        user.setPassword("");

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"status\":\"400 BAD_REQUEST\",\"errors\":[\"password must not be blank\"]}"));

        user.setFirstName("");
        user.setPassword(passwordEncoder.encode("12345678"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"status\":\"400 BAD_REQUEST\",\"errors\":[\"firstName must not be blank\"]}"));

        user.setLastName("");
        user.setFirstName("Marc");

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"status\":\"400 BAD_REQUEST\",\"errors\":[\"lastName must not be blank\"]}"));
    }

    @Test
    public void testLoginAndVerifyToken() throws Exception {
        User user = new User();
        user.setFirstName("Marc");
        user.setLastName("Scout");
        user.setEmail("marc.scout@lumen.com");
        user.setPassword(passwordEncoder.encode("12345678"));
        user.setRole(Role.CLIENT);
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest("marc.scout@lumen.com", "12345678");

        String loginPayload = objectMapper.writeValueAsString(loginRequest);
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseBody).get("message").asText();
        System.out.println(token);
        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

}
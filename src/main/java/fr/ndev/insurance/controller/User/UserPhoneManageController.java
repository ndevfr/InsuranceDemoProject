package fr.ndev.insurance.controller.User;

import fr.ndev.insurance.dto.PhoneDTO;
import fr.ndev.insurance.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent/users")
@Tag(name = "agent_users", description = "Endpoints for user management")
public class UserPhoneManageController {

    private final UserService userService;

    @Autowired
    UserPhoneManageController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Add phone number", description = "Add phone number to user profile")
    @PostMapping("/{userId}/phones")
    public ResponseEntity<?> addPhone(@RequestBody @Valid PhoneDTO phoneRequest, @PathVariable Long userId) {
        return userService.addPhone(phoneRequest, userId);
    }

    @Operation(summary = "Update phone number", description = "Update phone number in user profile")
    @PutMapping("/{userId}/phones/{id}")
    public ResponseEntity<?> updatePhone(@PathVariable int id, @RequestBody @Valid PhoneDTO phoneRequest, @PathVariable Long userId) {
        return userService.updatePhone(id, phoneRequest, userId);
    }

    @Operation(summary = "Set phone number as main", description = "Set phone number as main in user profile")
    @PutMapping("/{userId}/phones/{id}/main")
    public ResponseEntity<?> choosePhoneAsMain(@PathVariable int id, @PathVariable Long userId) {
        return userService.choosePhoneAsMain(id, userId);
    }

    @Operation(summary = "Delete phone number", description = "Delete phone number from user profile")
    @DeleteMapping("/{userId}/phones/{id}")
    public ResponseEntity<?> deletePhone(@PathVariable int id, @PathVariable Long userId) {
        return userService.deletePhone(id, userId);
    }

    @Operation(summary = "Get phone numbers", description = "Get phone numbers from user profile")
    @GetMapping("/{userId}/phones")
    public ResponseEntity<?> getAllPhones(@PathVariable Long userId) {
        return userService.getAllPhones(userId);
    }

}

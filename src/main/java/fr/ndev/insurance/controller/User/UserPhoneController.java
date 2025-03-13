package fr.ndev.insurance.controller.User;

import fr.ndev.insurance.dto.PhoneDTO;
import fr.ndev.insurance.exception.ExceptionResponse;
import fr.ndev.insurance.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "users", description = "Endpoints for user profile")
public class UserPhoneController {

    private final UserService userService;

    @Autowired
    UserPhoneController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Add phone number", description = "Add phone number to user profile")
    @PostMapping("/phones")
    public ResponseEntity<?> addPhone(@RequestBody @Valid PhoneDTO phoneRequest) {
        return userService.addPhone(phoneRequest, null);
    }

    @Operation(summary = "Update phone number", description = "Update phone number in user profile")
    @PutMapping("/phones/{id}")
    public ResponseEntity<?> updatePhone(@PathVariable int id, @RequestBody @Valid PhoneDTO phoneRequest) {
        return userService.updatePhone(id, phoneRequest, null);
    }

    @Operation(summary = "Set phone number as main", description = "Set phone number as main in user profile")
    @PutMapping("/phones/{id}/main")
    public ResponseEntity<?> choosePhoneAsMain(@PathVariable int id) {
        return userService.choosePhoneAsMain(id, null);
    }

    @Operation(summary = "Delete phone number", description = "Delete phone number from user profile")
    @DeleteMapping("/phones/{id}")
    public ResponseEntity<?> deletePhone(@PathVariable int id) {
        return userService.deletePhone(id, null);
    }

    @Operation(summary = "Get phone numbers", description = "Get phone numbers from user profile")
    @GetMapping("/phones")
    public ResponseEntity<?> getAllPhones() {
        return userService.getAllPhones(null);
    }

}

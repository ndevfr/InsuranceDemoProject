package fr.ndev.insurance.controller.User;

import fr.ndev.insurance.dto.AddressDTO;
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
public class UserAddressManageController {

    private final UserService userService;

    @Autowired
    UserAddressManageController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Add postal address", description = "Add postal address to user profile")
    @PostMapping("/{userId}/addresses")
    public ResponseEntity<?> addAddress(@RequestBody @Valid AddressDTO addressRequest, @PathVariable Long userId) {
        return userService.addAddress(addressRequest, userId);
    }

    @Operation(summary = "Update postal address", description = "Update postal address in user profile")
    @PutMapping("/{userId}/addresses/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable int id, @RequestBody @Valid AddressDTO addressRequest, @PathVariable Long userId) {
        return userService.updateAddress(id, addressRequest, userId);
    }

    @Operation(summary = "Set postal address as main", description = "Set postal address as main in user profile")
    @PutMapping("/{userId}/addresses/{id}/main")
    public ResponseEntity<?> chooseAddressAsMain(@PathVariable int id, @PathVariable Long userId) {
        return userService.chooseAddressAsMain(id, userId);
    }

    @Operation(summary = "Delete postal address", description = "Delete postal address from user profile")
    @DeleteMapping("/{userId}/addresses/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable int id, @PathVariable Long userId) {
        return userService.deleteAddress(id, userId);
    }

    @Operation(summary = "Get postal address", description = "Get postal address from user profile")
    @GetMapping("/{userId}/addresses")
    public ResponseEntity<?> getAllAddresses(@PathVariable Long userId) {
        return userService.getAllAddresses(userId);
    }

}

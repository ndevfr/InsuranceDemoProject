package fr.ndev.insurance.controller.User;

import fr.ndev.insurance.dto.AddressDTO;
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
public class UserAddressController {

    private final UserService userService;

    @Autowired
    UserAddressController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Add postal address", description = "Add postal address to user profile")
    @PostMapping("/addresses")
    public ResponseEntity<?> addAddress(@RequestBody @Valid AddressDTO addressRequest) {
        return userService.addAddress(addressRequest, null);
    }

    @Operation(summary = "Update postal address", description = "Update postal address in user profile")
    @PutMapping("/addresses/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable int id, @RequestBody @Valid AddressDTO addressRequest) {
        return userService.updateAddress(id, addressRequest, null);
    }

    @Operation(summary = "Set postal address as main", description = "Set postal address as main in user profile")
    @PutMapping("/addresses/{id}/main")
    public ResponseEntity<?> chooseAddressAsMain(@PathVariable int id) {
        return userService.chooseAddressAsMain(id, null);
    }

    @Operation(summary = "Delete postal address", description = "Delete postal address from user profile")
    @DeleteMapping("/addresses/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable int id) {
        return userService.deleteAddress(id, null);
    }

    @Operation(summary = "Get all postal address", description = "Get postal address from user profile")
    @GetMapping("/addresses")
    public ResponseEntity<?> getAllAddress() {
        return userService.getAllAddresses(null);
    }

}

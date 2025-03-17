package fr.ndev.insurance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class PasswordRequest {

    @NotBlank
    @Schema(type = "string", example = "12345678")
    private String currentPassword;

    @NotBlank
    @Schema(type = "string", example = "12345678")
    private String newPassword;

    @NotBlank
    @Schema(type = "string", example = "newPassWord")
    private String confirmPassword;

    public PasswordRequest() {}

    public PasswordRequest(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}

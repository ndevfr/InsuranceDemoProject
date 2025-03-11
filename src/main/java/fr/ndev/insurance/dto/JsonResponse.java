package fr.ndev.insurance.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.springframework.http.HttpStatus;

public class JsonResponse {

    private HttpStatus status;
    private String message;

    public JsonResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonGetter("status")
    public String formattedStatus() {
        return "%s".formatted(status);
    }

}

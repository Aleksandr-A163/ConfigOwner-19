package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class LoginResponseModel {

    private String username, userId, password, token, expires, isActive;
    @JsonProperty("created_date")
    private Date createdDate;
}
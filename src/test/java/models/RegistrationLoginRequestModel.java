package models;

import lombok.Data;

@Data
public class RegistrationLoginRequestModel {
    String userName, password;
}
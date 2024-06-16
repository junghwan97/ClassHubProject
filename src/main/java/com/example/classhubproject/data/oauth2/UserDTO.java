package com.example.classhubproject.data.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDTO {

    private String role;
    private String name;
    @JsonProperty("username")
    private String username;
    private String picture;
}
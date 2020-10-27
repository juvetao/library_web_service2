package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = 4213302687940653943L;

    @Id
    private String id;  // for mongodb
    @NotEmpty(message = "Firstname can not be empty")
    @Size(min = 2, max = 10, message = "Firstname length not valid")
    private String firstname;
    @NotEmpty(message = "Lastname can not be empty")
    @Size(min = 2, max = 10, message = "Lastname length not valid")
    private String lastname;
    @Past(message = "Birthday can not be present or in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate birthday;
    @Email(message = "E-mail address invalid")
    private String mail;
    @Pattern(regexp = "([0-9]){2,4}-([0-9]){5,8}", message = "Phone number not valid")
    private String phone;
    @Size(min = 4, max = 10, message = "Username length invalid")
    @NotBlank(message = "Username must contain a value ")
    @Indexed(unique = true)
    private String username;
    @Size(min = 4, max = 10, message = "Password length invalid")
    @NotBlank(message = "Password must contain a value ")
    private String password;
    private List<String> acl;

}

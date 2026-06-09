package com.balaji.distributor.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // REQUIRED FIELDS (Signup)

    @NotBlank(message = "Username is required")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    @Column(nullable = false, unique = true)
    private String mobileno;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    @JsonProperty(
            access = JsonProperty.Access.WRITE_ONLY
    )
    private String password;

    private String role = "USER";



    // OPTIONAL PROFILE FIELDS
    @Column(unique = true)
    private String email;

    private String shopname;

    private String gstnumber;


    // ADDRESS FIELDS

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private Long pincode;

    // for admin info
    private Integer totalOrders = 0;

    private Double totalPurchaseAmount = 0.0;

    private Boolean blocked  = false;


}

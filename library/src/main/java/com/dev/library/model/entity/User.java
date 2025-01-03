package com.dev.library.model.entity;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;

import com.dev.library.core.AppConstant.UserAccountRegex;
import com.dev.library.core.validation.StrongPassword;
import com.dev.library.core.validation.UserAccountElement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@UserAccountElement.List({
        @UserAccountElement(field = "email", regex = UserAccountRegex.EMAIL, message = "Email is not in correct format , please try again"),
        @UserAccountElement(field = "phoneNumber", regex = UserAccountRegex.PHONE_NUMBER, message = "PhoneNumber is not in correct format , please try again"),
})

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Length(min = 3, max = 50, message = "Fullname must be between 3 and 50 characters")
    private String fullname;
    private String email;
    @StrongPassword(message = "Incorrect password format . Please try other password")
    private String password;
    private String role;
    private String phoneNumber;
    private String status;
    private String avatar;
    private String address;
    private Integer delflag;
    private LocalDateTime create_time;
}

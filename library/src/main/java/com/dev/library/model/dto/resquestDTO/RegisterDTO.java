package com.dev.library.model.dto.resquestDTO;

import com.dev.library.core.validation.StrongPassword;
import com.dev.library.core.validation.UserAccountElement;

import org.hibernate.validator.constraints.Length;

import com.dev.library.core.AppConstant.UserAccountRegex;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@UserAccountElement.List({
        @UserAccountElement(field = "email", regex = UserAccountRegex.EMAIL, message = "Email is not in correct format , please try again"),
        @UserAccountElement(field = "phoneNumber", regex = UserAccountRegex.PHONE_NUMBER, message = "PhoneNumber is not in correct format , please try again"),
})

public class RegisterDTO {
    @Length(min = 3, max = 50, message = "Fullname must be between 3 and 50 characters")
    private String fullname;
    private String email;
    @StrongPassword
    private String password;
    private String phoneNumber;
    private String address;
}

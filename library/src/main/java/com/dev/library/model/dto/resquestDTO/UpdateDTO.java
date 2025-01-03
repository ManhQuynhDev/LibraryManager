package com.dev.library.model.dto.resquestDTO;

import org.hibernate.validator.constraints.Length;

import com.dev.library.core.AppConstant.UserAccountRegex;
import com.dev.library.core.validation.UserAccountElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@UserAccountElement.List({
        @UserAccountElement(field = "phoneNumber", regex = UserAccountRegex.PHONE_NUMBER, message = "PhoneNumber is not in correct format , please try again"),
})

public class UpdateDTO {
    @Length(min = 3, max = 50, message = "Fullname must be between 3 and 50 characters")
    private String fullname;
    private String phoneNumber;
    private String address;
}

package ar.com.dh.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequestDto {

    @NotNull(message = "The email can not be null" )
    @NotBlank(message = "The email can not be empty" )
    private String email;

    @NotNull(message = "The password can not be null" )
    @NotBlank(message = "The password can not be empty" )
    private String password;

    @NotNull()
    @NotBlank()
    private Boolean isAdmin;



}

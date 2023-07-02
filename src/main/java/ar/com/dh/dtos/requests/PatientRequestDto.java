package ar.com.dh.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientRequestDto {

    private Long idPatient;

    @NotNull(message = "The first name can not be null" )
    @NotBlank(message = "The first name can not be empty" )
    @Size(min = 3, message = "The first name must have at least 3 letters")
    private String firstName;

    @NotNull(message = "The last name can not be null" )
    @NotBlank(message = "The last name can not be empty" )
    @Size(min = 3, message = "The last name must have at least 3 letters")
    private String lastName;

    @NotNull(message = "The dni can not be null" )
    @NotBlank(message = "The dni can not be empty" )
    private String dni;

    @Valid
    private AddressRequestDto address;


}

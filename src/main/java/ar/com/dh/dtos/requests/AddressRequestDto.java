package ar.com.dh.dtos.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
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
public class AddressRequestDto {

    private Long idAddress;

    @NotNull(message = "The street can not be null" )
    @NotBlank(message = "The street can not be empty" )
    private String street;

    @NotNull(message = "The number can not be null" )
    @Positive(message = "The number must be greater than 0")
    private Integer number;

    @NotNull(message = "The town can not be null" )
    @NotBlank(message = "The town can not be empty" )
    private String town;

    @NotNull(message = "The province can not be null" )
    @NotBlank(message = "The province can not be empty" )
    private String province;

    @NotNull(message = "The postal code can not be null" )
    @Positive(message = "The number must be greater than 0")
    @Digits(integer = 4, fraction = 0, message = "The postal code must have 4 digits or less")
    private Integer postalCode;

    @NotNull(message = "The country can not be null" )
    @NotBlank(message = "The country can not be empty" )
    private String country;


}

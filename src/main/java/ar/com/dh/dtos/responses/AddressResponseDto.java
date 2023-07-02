package ar.com.dh.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class AddressResponseDto {

    private String street;
    private Integer number;
    private String town;
    private String province;
    private Integer postalCode;
    private String country;
}

package ar.com.dh.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class DentistResponseDto {

    private Long idDentist;
    private String firstName;
    private String lastName;
    private Long enrollment;

}

package uz.pdp.shopapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String username;
    private String fullName;
    private String passportNumber;
    private LocalDate issuedAt;
    private String bankAccount;
    private BigDecimal balance;

}

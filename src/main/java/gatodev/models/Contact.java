package gatodev.models;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private Long id;
    private String firstname;
    private String lastname;
    private String company;
    private String phoneNumber;
    private String email;
    private LocalDate birthDate;
    private String address;
}

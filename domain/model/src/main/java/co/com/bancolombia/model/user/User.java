package co.com.bancolombia.model.user;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String documentId;
    private Double baseSalary;


}

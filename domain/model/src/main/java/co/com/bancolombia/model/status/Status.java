package co.com.bancolombia.model.status;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Status {

    private Integer statusId;
    private String name;
    private String description;
}

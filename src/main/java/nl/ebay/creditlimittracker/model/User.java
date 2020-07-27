package nl.ebay.creditlimittracker.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.ebay.creditlimittracker.model.enums.BackendSystem;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class User extends RepresentationModel<User> {
    private String name;
    private String address;
    private String postCode;
    private String phone;
    private double creditLimit;
    private String dateOfBirth;
    private BackendSystem backendSystem;

}

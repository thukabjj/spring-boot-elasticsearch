package br.com.elasticsearch.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
    @JsonProperty("FirstName")
    @NotEmpty
    private String firstName;

    @JsonProperty("LastName")
    @NotEmpty
    private String lastName;

    @JsonProperty("Designation")
    @NotEmpty
    private String designation;

    @JsonProperty("Salary")
    @NotNull
    private Double salary;

    @JsonProperty("DateOfJoining")
    @NotEmpty
    private String dateOfJoining;

    @JsonProperty("Address")
    @NotEmpty
    private String address;

    @JsonProperty("Gender")
    @NotEmpty
    private String gender;

    @JsonProperty("Age")
    @NotNull
    private Integer age;

    @JsonProperty("MaritalStatus")
    @NotEmpty
    private String maritalStatus;

    @JsonProperty("Interests")
    @NotEmpty
    private String interests;
}

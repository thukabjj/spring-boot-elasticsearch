package br.com.elasticsearch.repository.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "customerindex")
public class CustomerEntity {
    @Id
    private String id;
    @Field(type = FieldType.Text, name = "firstName")
    private String firstName;
    @Field(type = FieldType.Text, name = "lastName")
    private String lastName;
    @Field(type = FieldType.Keyword, name = "designation")
    private String designation;
    @Field(type = FieldType.Double, name = "salary")
    private Double salary;
    @Field(type = FieldType.Date, name = "dateOfJoining")
    private String dateOfJoining;
    @Field(type = FieldType.Text, name = "address")
    private String address;
    @Field(type = FieldType.Text, name = "gender")
    private String gender;
    @Field(type = FieldType.Integer, name = "age")
    private Integer age;
    @Field(type = FieldType.Text, name = "maritalStatus")
    private String maritalStatus;
    @Field(type = FieldType.Keyword, name = "interests")
    private String interests;
}

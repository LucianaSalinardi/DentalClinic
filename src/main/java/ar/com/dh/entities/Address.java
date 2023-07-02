package ar.com.dh.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_address")
    private Long idAddress;

    @Column(name = "street_name")
    private String street;

    @Column(name = "street_number")
    private Integer number;

    @Column(name = "town")
    private String town;

    @Column(name = "province")
    private String province;

    @Column(name = "postal_code")
    private Integer postalCode;

    @Column(name = "country")
    private String country;

    public Address(String street, Integer number, String town, String province, Integer postalCode, String country) {
        this.street = street;
        this.number = number;
        this.town = town;
        this.province = province;
        this.postalCode = postalCode;
        this.country = country;
    }

    public void setIdAdress(Long idAdress) {
        this.idAddress = idAdress;
    }

}

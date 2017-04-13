package fr.afcepf.atod.shipping.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num")
    private String num;

    @Column(name = "address")
    private String address;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "lattitude")
    private Float lattitude;

    @Column(name = "longitude")
    private Float longitude;

    @OneToOne
    @JoinColumn(unique = true)
    private Region region;

    @OneToMany(mappedBy = "address")
    @JsonIgnore
    private Set<Command> commands = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public Address num(String num) {
        this.num = num;
        return this;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAddress() {
        return address;
    }

    public Address address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public Address zipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public Address city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public Address country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Float getLattitude() {
        return lattitude;
    }

    public Address lattitude(Float lattitude) {
        this.lattitude = lattitude;
        return this;
    }

    public void setLattitude(Float lattitude) {
        this.lattitude = lattitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public Address longitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Region getRegion() {
        return region;
    }

    public Address region(Region region) {
        this.region = region;
        return this;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Set<Command> getCommands() {
        return commands;
    }

    public Address commands(Set<Command> commands) {
        this.commands = commands;
        return this;
    }

    public Address addCommand(Command command) {
        this.commands.add(command);
        command.setAddress(this);
        return this;
    }

    public Address removeCommand(Command command) {
        this.commands.remove(command);
        command.setAddress(null);
        return this;
    }

    public void setCommands(Set<Command> commands) {
        this.commands = commands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        if (address.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Address{" +
            "id=" + id +
            ", num='" + num + "'" +
            ", address='" + address + "'" +
            ", zipCode='" + zipCode + "'" +
            ", city='" + city + "'" +
            ", country='" + country + "'" +
            ", lattitude='" + lattitude + "'" +
            ", longitude='" + longitude + "'" +
            '}';
    }
}

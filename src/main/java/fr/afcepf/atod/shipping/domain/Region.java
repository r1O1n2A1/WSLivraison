package fr.afcepf.atod.shipping.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Region.
 */
@Entity
@Table(name = "region")
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department")
    private Integer department;

    @ManyToOne
    private Postman postman;

    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDepartment() {
        return department;
    }

    public Region department(Integer department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public Postman getPostman() {
        return postman;
    }

    public Region postman(Postman postman) {
        this.postman = postman;
        return this;
    }

    public void setPostman(Postman postman) {
        this.postman = postman;
    }

    public Address getAddress() {
        return address;
    }

    public Region address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Region region = (Region) o;
        if (region.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, region.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Region{" +
            "id=" + id +
            ", department='" + department + "'" +
            '}';
    }
}

package fr.afcepf.atod.shipping.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Region entity.
 */
public class RegionDTO implements Serializable {

    private Long id;

    private Integer department;

    private Long postmanId;

    private Long addressId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(Integer department) {
        this.department = department;
    }

    public Long getPostmanId() {
        return postmanId;
    }

    public void setPostmanId(Long postmanId) {
        this.postmanId = postmanId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RegionDTO regionDTO = (RegionDTO) o;

        if ( ! Objects.equals(id, regionDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RegionDTO{" +
            "id=" + id +
            ", department='" + department + "'" +
            '}';
    }
}

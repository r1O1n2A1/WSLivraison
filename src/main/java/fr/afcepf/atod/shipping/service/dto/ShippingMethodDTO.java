package fr.afcepf.atod.shipping.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the ShippingMethod entity.
 */
public class ShippingMethodDTO implements Serializable {

    private Long id;

    private String label;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShippingMethodDTO shippingMethodDTO = (ShippingMethodDTO) o;

        if ( ! Objects.equals(id, shippingMethodDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ShippingMethodDTO{" +
            "id=" + id +
            ", label='" + label + "'" +
            '}';
    }
}

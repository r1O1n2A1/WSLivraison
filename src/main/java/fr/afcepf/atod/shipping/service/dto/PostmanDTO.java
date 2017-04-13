package fr.afcepf.atod.shipping.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Postman entity.
 */
public class PostmanDTO implements Serializable {

    private Long id;

    private String label;

    private String maxPrice;

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
    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PostmanDTO postmanDTO = (PostmanDTO) o;

        if ( ! Objects.equals(id, postmanDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PostmanDTO{" +
            "id=" + id +
            ", label='" + label + "'" +
            ", maxPrice='" + maxPrice + "'" +
            '}';
    }
}

package fr.afcepf.atod.shipping.service.dto;


import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Command entity.
 */
public class CommandDTO implements Serializable {

    private Long id;

    private ZonedDateTime dateOrder;

    private ZonedDateTime dateTaken;

    private ZonedDateTime dateShipping;

    private ZonedDateTime dateDelivery;

    private Long addressId;

    private Long postmanId;

    private Long shippingMethodId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(ZonedDateTime dateOrder) {
        this.dateOrder = dateOrder;
    }
    public ZonedDateTime getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(ZonedDateTime dateTaken) {
        this.dateTaken = dateTaken;
    }
    public ZonedDateTime getDateShipping() {
        return dateShipping;
    }

    public void setDateShipping(ZonedDateTime dateShipping) {
        this.dateShipping = dateShipping;
    }
    public ZonedDateTime getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(ZonedDateTime dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getPostmanId() {
        return postmanId;
    }

    public void setPostmanId(Long postmanId) {
        this.postmanId = postmanId;
    }

    public Long getShippingMethodId() {
        return shippingMethodId;
    }

    public void setShippingMethodId(Long shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommandDTO commandDTO = (CommandDTO) o;

        if ( ! Objects.equals(id, commandDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CommandDTO{" +
            "id=" + id +
            ", dateOrder='" + dateOrder + "'" +
            ", dateTaken='" + dateTaken + "'" +
            ", dateShipping='" + dateShipping + "'" +
            ", dateDelivery='" + dateDelivery + "'" +
            '}';
    }
}

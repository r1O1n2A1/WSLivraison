package fr.afcepf.atod.shipping.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Command.
 */
@Entity
@Table(name = "command")
public class Command implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_order")
    private ZonedDateTime dateOrder;

    @Column(name = "date_taken")
    private ZonedDateTime dateTaken;

    @Column(name = "date_shipping")
    private ZonedDateTime dateShipping;

    @Column(name = "date_delivery")
    private ZonedDateTime dateDelivery;

    @ManyToOne
    private Address address;

    @ManyToOne
    private Postman postman;

    @ManyToOne
    private ShippingMethod shippingMethod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateOrder() {
        return dateOrder;
    }

    public Command dateOrder(ZonedDateTime dateOrder) {
        this.dateOrder = dateOrder;
        return this;
    }

    public void setDateOrder(ZonedDateTime dateOrder) {
        this.dateOrder = dateOrder;
    }

    public ZonedDateTime getDateTaken() {
        return dateTaken;
    }

    public Command dateTaken(ZonedDateTime dateTaken) {
        this.dateTaken = dateTaken;
        return this;
    }

    public void setDateTaken(ZonedDateTime dateTaken) {
        this.dateTaken = dateTaken;
    }

    public ZonedDateTime getDateShipping() {
        return dateShipping;
    }

    public Command dateShipping(ZonedDateTime dateShipping) {
        this.dateShipping = dateShipping;
        return this;
    }

    public void setDateShipping(ZonedDateTime dateShipping) {
        this.dateShipping = dateShipping;
    }

    public ZonedDateTime getDateDelivery() {
        return dateDelivery;
    }

    public Command dateDelivery(ZonedDateTime dateDelivery) {
        this.dateDelivery = dateDelivery;
        return this;
    }

    public void setDateDelivery(ZonedDateTime dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    public Address getAddress() {
        return address;
    }

    public Command address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Postman getPostman() {
        return postman;
    }

    public Command postman(Postman postman) {
        this.postman = postman;
        return this;
    }

    public void setPostman(Postman postman) {
        this.postman = postman;
    }

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public Command shippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
        return this;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Command command = (Command) o;
        if (command.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, command.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Command{" +
            "id=" + id +
            ", dateOrder='" + dateOrder + "'" +
            ", dateTaken='" + dateTaken + "'" +
            ", dateShipping='" + dateShipping + "'" +
            ", dateDelivery='" + dateDelivery + "'" +
            '}';
    }
}

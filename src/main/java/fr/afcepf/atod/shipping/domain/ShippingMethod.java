package fr.afcepf.atod.shipping.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ShippingMethod.
 */
@Entity
@Table(name = "shipping_method")
public class ShippingMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_label")
    private String label;

    @OneToMany(mappedBy = "shippingMethod")
    @JsonIgnore
    private Set<Command> commands = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public ShippingMethod label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<Command> getCommands() {
        return commands;
    }

    public ShippingMethod commands(Set<Command> commands) {
        this.commands = commands;
        return this;
    }

    public ShippingMethod addCommand(Command command) {
        this.commands.add(command);
        command.setShippingMethod(this);
        return this;
    }

    public ShippingMethod removeCommand(Command command) {
        this.commands.remove(command);
        command.setShippingMethod(null);
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
        ShippingMethod shippingMethod = (ShippingMethod) o;
        if (shippingMethod.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, shippingMethod.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ShippingMethod{" +
            "id=" + id +
            ", label='" + label + "'" +
            '}';
    }
}

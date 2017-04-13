package fr.afcepf.atod.shipping.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Postman.
 */
@Entity
@Table(name = "postman")
public class Postman implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_label")
    private String label;

    @Column(name = "max_price")
    private String maxPrice;

    @OneToMany(mappedBy = "postman")
    @JsonIgnore
    private Set<Command> commands = new HashSet<>();

    @OneToMany(mappedBy = "postman")
    @JsonIgnore
    private Set<PriceTable> priceTables = new HashSet<>();

    @OneToMany(mappedBy = "postman")
    @JsonIgnore
    private Set<Region> regions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public Postman label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public Postman maxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
        return this;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Set<Command> getCommands() {
        return commands;
    }

    public Postman commands(Set<Command> commands) {
        this.commands = commands;
        return this;
    }

    public Postman addCommand(Command command) {
        this.commands.add(command);
        command.setPostman(this);
        return this;
    }

    public Postman removeCommand(Command command) {
        this.commands.remove(command);
        command.setPostman(null);
        return this;
    }

    public void setCommands(Set<Command> commands) {
        this.commands = commands;
    }

    public Set<PriceTable> getPriceTables() {
        return priceTables;
    }

    public Postman priceTables(Set<PriceTable> priceTables) {
        this.priceTables = priceTables;
        return this;
    }

    public Postman addPriceTables(PriceTable priceTable) {
        this.priceTables.add(priceTable);
        priceTable.setPostman(this);
        return this;
    }

    public Postman removePriceTables(PriceTable priceTable) {
        this.priceTables.remove(priceTable);
        priceTable.setPostman(null);
        return this;
    }

    public void setPriceTables(Set<PriceTable> priceTables) {
        this.priceTables = priceTables;
    }

    public Set<Region> getRegions() {
        return regions;
    }

    public Postman regions(Set<Region> regions) {
        this.regions = regions;
        return this;
    }

    public Postman addRegion(Region region) {
        this.regions.add(region);
        region.setPostman(this);
        return this;
    }

    public Postman removeRegion(Region region) {
        this.regions.remove(region);
        region.setPostman(null);
        return this;
    }

    public void setRegions(Set<Region> regions) {
        this.regions = regions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Postman postman = (Postman) o;
        if (postman.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, postman.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Postman{" +
            "id=" + id +
            ", label='" + label + "'" +
            ", maxPrice='" + maxPrice + "'" +
            '}';
    }
}

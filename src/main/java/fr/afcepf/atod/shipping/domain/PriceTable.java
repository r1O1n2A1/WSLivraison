package fr.afcepf.atod.shipping.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PriceTable.
 */
@Entity
@Table(name = "price_table")
public class PriceTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    private Float price;

    @Column(name = "range_low")
    private Integer rangeLow;

    @Column(name = "range_high")
    private Integer rangeHigh;

    @ManyToOne
    private Postman postman;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPrice() {
        return price;
    }

    public PriceTable price(Float price) {
        this.price = price;
        return this;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getRangeLow() {
        return rangeLow;
    }

    public PriceTable rangeLow(Integer rangeLow) {
        this.rangeLow = rangeLow;
        return this;
    }

    public void setRangeLow(Integer rangeLow) {
        this.rangeLow = rangeLow;
    }

    public Integer getRangeHigh() {
        return rangeHigh;
    }

    public PriceTable rangeHigh(Integer rangeHigh) {
        this.rangeHigh = rangeHigh;
        return this;
    }

    public void setRangeHigh(Integer rangeHigh) {
        this.rangeHigh = rangeHigh;
    }

    public Postman getPostman() {
        return postman;
    }

    public PriceTable postman(Postman postman) {
        this.postman = postman;
        return this;
    }

    public void setPostman(Postman postman) {
        this.postman = postman;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PriceTable priceTable = (PriceTable) o;
        if (priceTable.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, priceTable.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PriceTable{" +
            "id=" + id +
            ", price='" + price + "'" +
            ", rangeLow='" + rangeLow + "'" +
            ", rangeHigh='" + rangeHigh + "'" +
            '}';
    }
}

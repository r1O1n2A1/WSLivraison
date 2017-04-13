package fr.afcepf.atod.shipping.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the PriceTable entity.
 */
public class PriceTableDTO implements Serializable {

    private Long id;

    private Float price;

    private Integer rangeLow;

    private Integer rangeHigh;

    private Long postmanId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
    public Integer getRangeLow() {
        return rangeLow;
    }

    public void setRangeLow(Integer rangeLow) {
        this.rangeLow = rangeLow;
    }
    public Integer getRangeHigh() {
        return rangeHigh;
    }

    public void setRangeHigh(Integer rangeHigh) {
        this.rangeHigh = rangeHigh;
    }

    public Long getPostmanId() {
        return postmanId;
    }

    public void setPostmanId(Long postmanId) {
        this.postmanId = postmanId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PriceTableDTO priceTableDTO = (PriceTableDTO) o;

        if ( ! Objects.equals(id, priceTableDTO.id)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PriceTableDTO{" +
            "id=" + id +
            ", price='" + price + "'" +
            ", rangeLow='" + rangeLow + "'" +
            ", rangeHigh='" + rangeHigh + "'" +
            '}';
    }
}

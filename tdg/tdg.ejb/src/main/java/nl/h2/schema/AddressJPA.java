package nl.h2.schema;

import javax.persistence.*;

/**
 * Created by: J. van Riemsdijk | H2
 */
@Entity
@Table(name = "address", schema = "public", catalog = "postgres")
public class AddressJPA {

    private Long addressId;
    private String street;
    private String zipcode;
    private Integer number;
    private String numberAddon;
    private String city;
    private String country;
    private BagJPA bag;


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }


    @Basic
    @Column(name = "street", length = -1)
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


    @Basic
    @Column(name = "zipcode", length = -1)
    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


    @Basic
    @Column(name = "number")
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }


    @Basic
    @Column(name = "number_addon", length = -1)
    public String getNumberAddon() {
        return numberAddon;
    }

    public void setNumberAddon(String numberAddon) {
        this.numberAddon = numberAddon;
    }


    @Basic
    @Column(name = "city", length = -1)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    @Basic
    @Column(name = "country", length = 254)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL)
    public BagJPA getBag() {
        return bag;
    }

    public void setBag(BagJPA bag) {
        this.bag = bag;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressJPA that = (AddressJPA) o;

        if (getAddressId() != null ? !getAddressId().equals(that.getAddressId()) : that.getAddressId() != null) return false;
        if (getStreet() != null ? !getStreet().equals(that.getStreet()) : that.getStreet() != null) return false;
        if (getZipcode() != null ? !getZipcode().equals(that.getZipcode()) : that.getZipcode() != null) return false;
        if (getNumber() != null ? !getNumber().equals(that.getNumber()) : that.getNumber() != null) return false;
        if (getNumberAddon() != null ? !getNumberAddon().equals(that.getNumberAddon()) : that.getNumberAddon() != null) return false;
        if (getCity() != null ? !getCity().equals(that.getCity()) : that.getCity() != null) return false;
        if (getCountry() != null ? !getCountry().equals(that.getCountry()) : that.getCountry() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getAddressId() != null ? getAddressId().hashCode() : 0;
        result = 31 * result + (getStreet() != null ? getStreet().hashCode() : 0);
        result = 31 * result + (getZipcode() != null ? getZipcode().hashCode() : 0);
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        result = 31 * result + (getNumberAddon() != null ? getNumberAddon().hashCode() : 0);
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        return result;
    }
}

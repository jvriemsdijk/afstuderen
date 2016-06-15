package nl.h2.ejb.schema;

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


    @OneToOne(mappedBy = "address")
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

        if (addressId != null ? !addressId.equals(that.addressId) : that.addressId != null) return false;
        if (street != null ? !street.equals(that.street) : that.street != null) return false;
        if (zipcode != null ? !zipcode.equals(that.zipcode) : that.zipcode != null) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        if (numberAddon != null ? !numberAddon.equals(that.numberAddon) : that.numberAddon != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = addressId != null ? addressId.hashCode() : 0;
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (zipcode != null ? zipcode.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (numberAddon != null ? numberAddon.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }
}

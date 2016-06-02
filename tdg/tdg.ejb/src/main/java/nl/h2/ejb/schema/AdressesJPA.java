package nl.h2.ejb.schema;

import javax.persistence.*;

/**
 * Created by joeyvanriemsdijk on 02/06/16.
 */
@Entity
@Table(name = "adresses", schema = "public", catalog = "postgres")
public class AdressesJPA {
    private long id;
    private String street;
    private String zipcode;
    private int number;
    private String numberAddon;
    private String city;
    private String country;
    private BagJPA bag;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "street", nullable = true, length = -1)
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Basic
    @Column(name = "zipcode", nullable = true, length = -1)
    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Basic
    @Column(name = "number", nullable = true)
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Basic
    @Column(name = "number_addon", nullable = true, length = -1)
    public String getNumberAddon() {
        return numberAddon;
    }

    public void setNumberAddon(String numberAddon) {
        this.numberAddon = numberAddon;
    }

    @Basic
    @Column(name = "city", nullable = true, length = -1)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "country", nullable = true, length = 254)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdressesJPA that = (AdressesJPA) o;

        if (id != that.id) return false;
        if (number != that.number) return false;
        if (street != null ? !street.equals(that.street) : that.street != null) return false;
        if (zipcode != null ? !zipcode.equals(that.zipcode) : that.zipcode != null) return false;
        if (numberAddon != null ? !numberAddon.equals(that.numberAddon) : that.numberAddon != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (zipcode != null ? zipcode.hashCode() : 0);
        result = 31 * result + number;
        result = 31 * result + (numberAddon != null ? numberAddon.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "adress", nullable = false)
    public BagJPA getBag() {
        return bag;
    }

    public void setBag(BagJPA bag) {
        this.bag = bag;
    }
}

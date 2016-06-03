package nl.h2.ejb.schema;

import javax.persistence.*;

/**
 * Created by joeyvanriemsdijk on 02/06/16.
 */
@Entity
@Table(name = "adresses", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "Adress.findAll", query = "SELECT a FROM AdressesJPA a"),
        @NamedQuery(name = "Adress.deleteAll", query = "DELETE FROM AdressesJPA")
})
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

    @OneToOne(mappedBy = "adress")
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

        AdressesJPA that = (AdressesJPA) o;

        if (getId() != that.getId()) return false;
        if (getNumber() != that.getNumber()) return false;
        if (getStreet() != null ? !getStreet().equals(that.getStreet()) : that.getStreet() != null) return false;
        if (getZipcode() != null ? !getZipcode().equals(that.getZipcode()) : that.getZipcode() != null) return false;
        if (getNumberAddon() != null ? !getNumberAddon().equals(that.getNumberAddon()) : that.getNumberAddon() != null)
            return false;
        if (getCity() != null ? !getCity().equals(that.getCity()) : that.getCity() != null) return false;
        if (getCountry() != null ? !getCountry().equals(that.getCountry()) : that.getCountry() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getStreet() != null ? getStreet().hashCode() : 0);
        result = 31 * result + (getZipcode() != null ? getZipcode().hashCode() : 0);
        result = 31 * result + getNumber();
        result = 31 * result + (getNumberAddon() != null ? getNumberAddon().hashCode() : 0);
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        return result;
    }
}

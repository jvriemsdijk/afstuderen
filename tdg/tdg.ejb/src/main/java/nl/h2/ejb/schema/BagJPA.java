package nl.h2.ejb.schema;

import javax.persistence.*;

/**
 * Created by joeyvanriemsdijk on 02/06/16.
 */
@Entity
@Table(name = "bag", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "Bag.findAll", query = "SELECT b FROM BagJPA b"),
        @NamedQuery(name = "Bag.deleteAll", query = "DELETE FROM BagJPA")
})
public class BagJPA {
    private long id;
    private short buildYear;
    private String buildingContour;
    private Double usableSurface;
    private String usePurpose;
    private String coordinates;
    private AdressJPA adress;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "build_year", nullable = false)
    public short getBuildYear() {
        return buildYear;
    }

    public void setBuildYear(short buildYear) {
        this.buildYear = buildYear;
    }

    @Basic
    @Column(name = "building_contour", nullable = true, length = -1)
    public String getBuildingContour() {
        return buildingContour;
    }

    public void setBuildingContour(String buildingContour) {
        this.buildingContour = buildingContour;
    }

    @Basic
    @Column(name = "usable_surface", nullable = true, precision = 0)
    public Double getUsableSurface() {
        return usableSurface;
    }

    public void setUsableSurface(Double usableSurface) {
        this.usableSurface = usableSurface;
    }

    @Basic
    @Column(name = "use_purpose", nullable = true, length = -1)
    public String getUsePurpose() {
        return usePurpose;
    }

    public void setUsePurpose(String usePurpose) {
        this.usePurpose = usePurpose;
    }

    @Basic
    @Column(name = "coordinates", nullable = true, length = -1)
    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adress", referencedColumnName = "id", nullable = false)
    public AdressJPA getAdress() {
        return adress;
    }

    public void setAdress(AdressJPA adress) {
        this.adress = adress;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BagJPA bagJPA = (BagJPA) o;

        if (getId() != bagJPA.getId()) return false;
        if (getBuildYear() != bagJPA.getBuildYear()) return false;
        if (getBuildingContour() != null ? !getBuildingContour().equals(bagJPA.getBuildingContour()) : bagJPA.getBuildingContour() != null)
            return false;
        if (getUsableSurface() != null ? !getUsableSurface().equals(bagJPA.getUsableSurface()) : bagJPA.getUsableSurface() != null)
            return false;
        if (getUsePurpose() != null ? !getUsePurpose().equals(bagJPA.getUsePurpose()) : bagJPA.getUsePurpose() != null)
            return false;
        if (getCoordinates() != null ? !getCoordinates().equals(bagJPA.getCoordinates()) : bagJPA.getCoordinates() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (int) getBuildYear();
        result = 31 * result + (getBuildingContour() != null ? getBuildingContour().hashCode() : 0);
        result = 31 * result + (getUsableSurface() != null ? getUsableSurface().hashCode() : 0);
        result = 31 * result + (getUsePurpose() != null ? getUsePurpose().hashCode() : 0);
        result = 31 * result + (getCoordinates() != null ? getCoordinates().hashCode() : 0);
        return result;
    }
}

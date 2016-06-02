package nl.h2.ejb.schema;

import javax.persistence.*;

/**
 * Created by joeyvanriemsdijk on 02/06/16.
 */
@Entity
@Table(name = "bag", schema = "public", catalog = "postgres")
public class BagJPA {
    private long id;
    private short buildYear;
    private String buildingContour;
    private Double usableSurface;
    private String usePurpose;
    private String coordinates;
    private AdressesJPA adress;

    @Id
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BagJPA bagJPA = (BagJPA) o;

        if (id != bagJPA.id) return false;
        if (buildYear != bagJPA.buildYear) return false;
        if (buildingContour != null ? !buildingContour.equals(bagJPA.buildingContour) : bagJPA.buildingContour != null)
            return false;
        if (usableSurface != null ? !usableSurface.equals(bagJPA.usableSurface) : bagJPA.usableSurface != null)
            return false;
        if (usePurpose != null ? !usePurpose.equals(bagJPA.usePurpose) : bagJPA.usePurpose != null) return false;
        if (coordinates != null ? !coordinates.equals(bagJPA.coordinates) : bagJPA.coordinates != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) buildYear;
        result = 31 * result + (buildingContour != null ? buildingContour.hashCode() : 0);
        result = 31 * result + (usableSurface != null ? usableSurface.hashCode() : 0);
        result = 31 * result + (usePurpose != null ? usePurpose.hashCode() : 0);
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        return result;
    }

    @OneToOne(mappedBy = "bag")
    public AdressesJPA getAdress() {
        return adress;
    }

    public void setAdress(AdressesJPA adress) {
        this.adress = adress;
    }
}

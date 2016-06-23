package nl.h2.schema;

import javax.persistence.*;

/**
 * Created by: J. van Riemsdijk | H2
 */
@Entity
@Table(name = "bag", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "Bag.findAll", query = "SELECT b FROM BagJPA b")
})
public class BagJPA {

    private Long bagId;
    private short buildYear;
    private String buildingContour;
    private Double usableSurface;
    private String usePurpose;
    private Double x;
    private Double y;
    private AddressJPA address;
    private HousingSituationJPA housingSituation;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bag_id", nullable = false)
    public Long getBagId() {
        return bagId;
    }

    public void setBagId(Long bagId) {
        this.bagId = bagId;
    }


    @Basic
    @Column(name = "x")
    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }


    @Basic
    @Column(name = "y")
    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address", referencedColumnName = "address_id", nullable = false)
    public AddressJPA getAddress() {
        return address;
    }

    public void setAddress(AddressJPA address) {
        this.address = address;
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
    @Column(name = "building_contour", length = -1)
    public String getBuildingContour() {
        return buildingContour;
    }

    public void setBuildingContour(String buildingContour) {
        this.buildingContour = buildingContour;
    }


    @Basic
    @Column(name = "usable_surface")
    public Double getUsableSurface() {
        return usableSurface;
    }

    public void setUsableSurface(Double usableSurface) {
        this.usableSurface = usableSurface;
    }


    @Basic
    @Column(name = "use_purpose", length = -1)
    public String getUsePurpose() {
        return usePurpose;
    }

    public void setUsePurpose(String usePurpose) {
        this.usePurpose = usePurpose;
    }


    @OneToOne(mappedBy = "bag", cascade = CascadeType.ALL)
    public HousingSituationJPA getHousingSituation() {
        return housingSituation;
    }

    public void setHousingSituation(HousingSituationJPA housingSituation) {
        this.housingSituation = housingSituation;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BagJPA bagJPA = (BagJPA) o;

        if (getBagId().equals(bagJPA.getBagId())) return false;
        if (getBuildYear() != bagJPA.getBuildYear()) return false;
        if (getBuildingContour() != null ? !getBuildingContour().equals(bagJPA.getBuildingContour()) : bagJPA.getBuildingContour() != null)
            return false;
        if (getUsableSurface() != null ? !getUsableSurface().equals(bagJPA.getUsableSurface()) : bagJPA.getUsableSurface() != null)
            return false;
        if (getUsePurpose() != null ? !getUsePurpose().equals(bagJPA.getUsePurpose()) : bagJPA.getUsePurpose() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 32;
        result = 31 * result + (int) getBuildYear();
        result = 31 * result + (getBuildingContour() != null ? getBuildingContour().hashCode() : 0);
        result = 31 * result + (getUsableSurface() != null ? getUsableSurface().hashCode() : 0);
        result = 31 * result + (getUsePurpose() != null ? getUsePurpose().hashCode() : 0);
        return result;
    }
}

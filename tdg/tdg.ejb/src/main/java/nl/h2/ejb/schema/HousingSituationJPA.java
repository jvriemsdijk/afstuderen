package nl.h2.ejb.schema;

import javax.persistence.*;
import java.util.List;

/**
 * Created by: J. van Riemsdijk | H2
 */
@Entity
@Table(name = "housing_situation", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "HousingSituation.findAll", query = "SELECT a FROM HousingSituationJPA a")
})
public class HousingSituationJPA {

    private Long housingSituationId;
    private short floor;
    private boolean elevator;
    private List<PersonToHousingSituationJPA> residents;
    private List<HousingSituationToAdjustmentJPA> placedAdjustments;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "housing_situation_id", nullable = false)
    public Long getHousingSituationId() {
        return housingSituationId;
    }

    public void setHousingSituationId(Long housingSituationId) {
        this.housingSituationId = housingSituationId;
    }


    @Column(name = "floor", nullable = false)
    public short getFloor() {
        return floor;
    }

    public void setFloor(short floor) {
        this.floor = floor;
    }


    @Basic
    @Column(name = "elevator", nullable = false)
    public boolean isElevator() {
        return elevator;
    }

    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }


    @OneToMany(mappedBy = "housingSituation")
    public List<HousingSituationToAdjustmentJPA> getPlacedAdjustments() {
        return placedAdjustments;
    }

    public void setPlacedAdjustments(List<HousingSituationToAdjustmentJPA> placedAdjustments) {
        this.placedAdjustments = placedAdjustments;
    }


    @OneToMany(mappedBy = "housingSituation")
    public List<PersonToHousingSituationJPA> getResidents() {
        return this.residents;
    }

    public void setResidents(List<PersonToHousingSituationJPA> residents) {
        this.residents = residents;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HousingSituationJPA that = (HousingSituationJPA) o;

        if (getHousingSituationId().equals(that.getHousingSituationId())) return false;
        if (getFloor() != that.getFloor()) return false;
        if (isElevator() != that.isElevator()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 32;
        result = 31 * result + (int) getFloor();
        result = 31 * result + (isElevator() ? 1 : 0);
        return result;
    }


}

package nl.h2.ejb.schema;

import javax.persistence.*;
import java.util.List;

/**
 * Created by joeyvanriemsdijk on 02/06/16.
 */
@Entity
@Table(name = "housing_situation", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "HousingSituation.findAll", query = "SELECT a FROM HousingSituationJPA a"),
        @NamedQuery(name = "HousingSituation.deleteAll", query = "DELETE FROM HousingSituationJPA")
})
public class HousingSituationJPA {
    private long id;
    private short floor;
    private boolean elevator;
    private List<AdjustmentJPA> adjustments;
    private List<PersonJPA> residents;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
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

    @OneToMany
    public List<AdjustmentJPA> getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(List<AdjustmentJPA> adjustments) {
        this.adjustments = adjustments;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "residents", catalog = "postgres", schema = "public", joinColumns = @JoinColumn(name = "housing_situation", referencedColumnName = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "person", referencedColumnName = "bsn", nullable = false))
    public List<PersonJPA> getResidents() {
        return residents;
    }

    public void setResidents(List<PersonJPA> residents) {
        this.residents = residents;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HousingSituationJPA that = (HousingSituationJPA) o;

        if (getId() != that.getId()) return false;
        if (getFloor() != that.getFloor()) return false;
        if (isElevator() != that.isElevator()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (int) getFloor();
        result = 31 * result + (isElevator() ? 1 : 0);
        return result;
    }
}

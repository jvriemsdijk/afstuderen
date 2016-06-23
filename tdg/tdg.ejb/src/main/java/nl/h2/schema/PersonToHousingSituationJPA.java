package nl.h2.schema;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: J. van Riemsdijk | H2
 */
@Entity
@Table(name = "person_to_housing_situation", schema = "public", catalog = "postgres")
public class PersonToHousingSituationJPA {

    private Date moveInDate;
    private Date moveOutDate;
    private HousingSituationJPA housingSituation;
    private PersonJPA resident;

    @Basic
    @Column(name = "move_in_date")
    public Date getMoveInDate() {
        return moveInDate;
    }

    public void setMoveInDate(Date moveInDate) {
        this.moveInDate = moveInDate;
    }


    @Basic
    @Column(name = "move_out_date", nullable = true)
    public Date getMoveOutDate() {
        return moveOutDate;
    }

    public void setMoveOutDate(Date moveOutDate) {
        this.moveOutDate = moveOutDate;
    }


    @ManyToOne
    @JoinColumn(name = "housing_situation", referencedColumnName = "housing_situation_id", nullable = false)
    public HousingSituationJPA getHousingSituation() {
        return housingSituation;
    }

    public void setHousingSituation(HousingSituationJPA housingSituation) {
        this.housingSituation = housingSituation;
    }


    @ManyToOne
    @JoinColumn(name = "person", referencedColumnName = "bsn", nullable = false)
    public PersonJPA getResident() {
        return resident;
    }

    public void setResident(PersonJPA resident) {
        this.resident = resident;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonToHousingSituationJPA that = (PersonToHousingSituationJPA) o;

        if (getMoveInDate() != null ? !getMoveInDate().equals(that.getMoveInDate()) : that.getMoveInDate() != null) return false;
        if (getMoveOutDate() != null ? !getMoveOutDate().equals(that.getMoveOutDate()) : that.getMoveOutDate() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getMoveInDate() != null ? getMoveInDate().hashCode() : 0;
        result = 31 * result + (getMoveOutDate() != null ? getMoveOutDate().hashCode() : 0);
        return result;
    }
}
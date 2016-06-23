package nl.h2.schema;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: J. van Riemsdijk | H2
 */
@Entity
@Table(name = "housing_situation_to_adjustment", schema = "public", catalog = "postgres")
public class HousingSituationToAdjustmentJPA {

    private Date datePlaced;
    private Date dateRemoved;
    private AdjustmentJPA adjustment;
    private HousingSituationJPA housingSituation;


    @Basic
    @Column(name = "date_placed", nullable = true)
    public Date getDatePlaced() {
        return datePlaced;
    }

    public void setDatePlaced(Date datePlaced) {
        this.datePlaced = datePlaced;
    }


    @Basic
    @Column(name = "date_removed", nullable = true)
    public Date getDateRemoved() {
        return dateRemoved;
    }

    public void setDateRemoved(Date dateRemoved) {
        this.dateRemoved = dateRemoved;
    }


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adjustment", referencedColumnName = "adjustment_id", nullable = false)
    public AdjustmentJPA getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(AdjustmentJPA adjustment) {
        this.adjustment = adjustment;
    }


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "housing_situation", referencedColumnName = "housing_situation_id", nullable = false)
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

        HousingSituationToAdjustmentJPA that = (HousingSituationToAdjustmentJPA) o;

        if (getDatePlaced() != null ? !getDatePlaced().equals(that.getDatePlaced()) : that.getDatePlaced() != null) return false;
        if (getDateRemoved() != null ? !getDateRemoved().equals(that.getDateRemoved()) : that.getDateRemoved() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getDatePlaced() != null ? getDatePlaced().hashCode() : 0;
        result = 31 * result + (getDateRemoved() != null ? getDateRemoved().hashCode() : 0);
        return result;
    }
}

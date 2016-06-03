package nl.h2.ejb.schema;

import javax.persistence.*;

/**
 * Created by joeyvanriemsdijk on 02/06/16.
 */
@Entity
@Table(name = "adjustment", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "Adjustment.findAll", query = "SELECT a FROM AdjustmentJPA a"),
        @NamedQuery(name = "Adjustment.deleteAll", query = "DELETE FROM AdjustmentJPA")
})
public class AdjustmentJPA {
    private long id;
    private Double actualCost;
    private AdjustmentDefinitionJPA adjustmentDefinition;
    private WmoDecisionJPA decision;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "actual_cost", nullable = true, precision = 0)
    public Double getActualCost() {
        return actualCost;
    }

    public void setActualCost(Double actualCost) {
        this.actualCost = actualCost;
    }

    @OneToOne
    @JoinColumn(name = "adjustment_definition", referencedColumnName = "id", nullable = false)
    public AdjustmentDefinitionJPA getAdjustmentDefinition() {
        return adjustmentDefinition;
    }

    public void setAdjustmentDefinition(AdjustmentDefinitionJPA adjustmentDefinition) {
        this.adjustmentDefinition = adjustmentDefinition;
    }

    @ManyToOne
    @JoinColumn(name = "wmo_decision", referencedColumnName = "id")
    public WmoDecisionJPA getDecision() {
        return decision;
    }

    public void setDecision(WmoDecisionJPA decision) {
        this.decision = decision;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdjustmentJPA that = (AdjustmentJPA) o;

        if (getId() != that.getId()) return false;
        if (getActualCost() != null ? !getActualCost().equals(that.getActualCost()) : that.getActualCost() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getActualCost() != null ? getActualCost().hashCode() : 0);
        return result;
    }
}

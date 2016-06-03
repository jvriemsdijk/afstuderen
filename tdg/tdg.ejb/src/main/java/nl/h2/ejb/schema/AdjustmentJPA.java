package nl.h2.ejb.schema;

import javax.persistence.*;

/**
 * Created by joeyvanriemsdijk on 02/06/16.
 */
@Entity
@Table(name = "adjustment", schema = "public", catalog = "postgres")
public class AdjustmentJPA {
    private long id;
    private Double actualCost;
    private AdjustmentDefinitionJPA adjustmentDefinition;
    private WmoDecisionsJPA decision;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdjustmentJPA that = (AdjustmentJPA) o;

        if (id != that.id) return false;
        if (actualCost != null ? !actualCost.equals(that.actualCost) : that.actualCost != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (actualCost != null ? actualCost.hashCode() : 0);
        return result;
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
    public WmoDecisionsJPA getDecision() {
        return decision;
    }

    public void setDecision(WmoDecisionsJPA decision) {
        this.decision = decision;
    }
}

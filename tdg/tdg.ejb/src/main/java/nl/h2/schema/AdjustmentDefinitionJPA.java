package nl.h2.schema;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by: J. van Riemsdijk | H2
 */
@Entity
@Table(name = "adjustment_definition", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "AdjustmentDefinition.findAll", query = "SELECT a FROM AdjustmentDefinitionJPA a")
})
public class AdjustmentDefinitionJPA {

    private Long adjustmentDefinitionId;
    private String name;
    private double averageCost;
    private Double costMargin;
    private List<ConditionJPA> conditions;
    private Date creationDate;
    private Integer version;


    @Id
    @Column(name = "adjustment_definition_id", nullable = false)
    public Long getAdjustmentDefinitionId() {
        return adjustmentDefinitionId;
    }

    public void setAdjustmentDefinitionId(Long adjustmentDefinitionId) {
        this.adjustmentDefinitionId = adjustmentDefinitionId;
    }


    @Basic
    @Column(name = "name", nullable = false, length = 254)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Basic
    @Column(name = "average_cost")
    public double getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(double averageCost) {
        this.averageCost = averageCost;
    }


    @Basic
    @Column(name = "cost_margin")
    public Double getCostMargin() {
        return costMargin;
    }

    public void setCostMargin(Double costMargin) {
        this.costMargin = costMargin;
    }


    @ManyToMany
    @JoinTable(name = "adjustment_definition_to_condition", catalog = "postgres", schema = "public", joinColumns = @JoinColumn(name = "adjustment_definition", referencedColumnName = "adjustment_definition_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "condition", referencedColumnName = "condition_id", nullable = false))
    public List<ConditionJPA> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionJPA> conditions) {
        this.conditions = conditions;
    }


    @Basic
    @Column(name = "creation_date")
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    @Basic
    @Column(name = "version")
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdjustmentDefinitionJPA that = (AdjustmentDefinitionJPA) o;

        if (getAdjustmentDefinitionId().equals(that.getAdjustmentDefinitionId())) return false;
        if (Double.compare(that.getAverageCost(), getAverageCost()) != 0) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getCostMargin() != null ? !getCostMargin().equals(that.getCostMargin()) : that.getCostMargin() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (getAdjustmentDefinitionId() ^ (getAdjustmentDefinitionId() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        temp = Double.doubleToLongBits(getAverageCost());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getCostMargin() != null ? getCostMargin().hashCode() : 0);
        return result;
    }
}

package nl.h2.ejb.schema;

import javax.persistence.*;
import java.util.List;

/**
 * Created by joeyvanriemsdijk on 02/06/16.
 */
@Entity
@Table(name = "adjustment_definitions", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "AdjustmentDefinition.findAll", query = "SELECT a FROM AdjustmentDefinitionJPA a"),
        @NamedQuery(name = "AdjustmentDefinition.deleteAll", query = "DELETE FROM AdjustmentDefinitionJPA")
})
public class AdjustmentDefinitionJPA {
    private long id;
    private String name;
    private double averageCost;
    private Double costMargin;
    private List<ConditionJPA> conditions;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
    @Column(name = "average_cost", nullable = false, precision = 0)
    public double getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(double averageCost) {
        this.averageCost = averageCost;
    }

    @Basic
    @Column(name = "cost_margin", nullable = true, precision = 0)
    public Double getCostMargin() {
        return costMargin;
    }

    public void setCostMargin(Double costMargin) {
        this.costMargin = costMargin;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "adjustment_condition", catalog = "postgres", schema = "public", joinColumns = @JoinColumn(name = "adjustment", referencedColumnName = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "condition", referencedColumnName = "id", nullable = false))
    public List<ConditionJPA> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionJPA> conditions) {
        this.conditions = conditions;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdjustmentDefinitionJPA that = (AdjustmentDefinitionJPA) o;

        if (getId() != that.getId()) return false;
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
        result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        temp = Double.doubleToLongBits(getAverageCost());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getCostMargin() != null ? getCostMargin().hashCode() : 0);
        return result;
    }
}

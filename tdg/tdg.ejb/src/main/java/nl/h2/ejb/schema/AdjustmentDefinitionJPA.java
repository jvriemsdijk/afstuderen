package nl.h2.ejb.schema;

import javax.persistence.*;
import java.util.List;

/**
 * Created by joeyvanriemsdijk on 02/06/16.
 */
@Entity
@Table(name = "adjustment_definitions", schema = "public", catalog = "postgres")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdjustmentDefinitionJPA that = (AdjustmentDefinitionJPA) o;

        if (id != that.id) return false;
        if (Double.compare(that.averageCost, averageCost) != 0) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (costMargin != null ? !costMargin.equals(that.costMargin) : that.costMargin != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(averageCost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (costMargin != null ? costMargin.hashCode() : 0);
        return result;
    }

    @ManyToMany(mappedBy = "adjustments")
    public List<ConditionJPA> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionJPA> conditions) {
        this.conditions = conditions;
    }
}

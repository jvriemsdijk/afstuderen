package nl.h2.schema;

import javax.persistence.*;
import java.util.List;

/**
 * Created by: J. van Riemsdijk | H2
 */
@Entity
@Table(name = "condition", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "Condition.findAll", query = "SELECT c FROM ConditionJPA c")
})
public class ConditionJPA {

    private Long conditionId;
    private String name;
    private boolean chronic;
    private List<AdjustmentDefinitionJPA> adjustmentDefinitions;

    @Id
    @Column(name = "condition_id", nullable = false)
    public Long getConditionId() {
        return conditionId;
    }

    public void setConditionId(Long conditionId) {
        this.conditionId = conditionId;
    }


    @Basic
    @Column(name = "chronic", nullable = false)
    public boolean isChronic() {
        return chronic;
    }

    public void setChronic(boolean chronic) {
        this.chronic = chronic;
    }


    @Basic
    @Column(name = "name", length = 254)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @ManyToMany(mappedBy = "conditions")
    public List<AdjustmentDefinitionJPA> getAdjustmentDefinitions() {
        return adjustmentDefinitions;
    }

    public void setAdjustmentDefinitions(List<AdjustmentDefinitionJPA> adjustmentDefinitions) {
        this.adjustmentDefinitions = adjustmentDefinitions;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConditionJPA that = (ConditionJPA) o;

        if (getConditionId().equals(that.getConditionId())) return false;
        if (isChronic() != that.isChronic()) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getConditionId() ^ (getConditionId() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (isChronic() ? 1 : 0);
        return result;
    }
}

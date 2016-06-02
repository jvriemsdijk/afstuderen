package nl.h2.ejb.schema;

import javax.persistence.*;
import java.util.List;

/**
 * Created by joeyvanriemsdijk on 02/06/16.
 */
@Entity
@Table(name = "conditions", schema = "public", catalog = "postgres")
public class ConditionJPA {
    private long id;
    private String name;
    private boolean chronic;
    private List<AdjustmentDefinitionJPA> adjustments;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 254)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "chronic", nullable = false)
    public boolean isChronic() {
        return chronic;
    }

    public void setChronic(boolean chronic) {
        this.chronic = chronic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConditionJPA that = (ConditionJPA) o;

        if (id != that.id) return false;
        if (chronic != that.chronic) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (chronic ? 1 : 0);
        return result;
    }

    @ManyToMany
    @JoinTable(name = "adjustment_condition", catalog = "postgres", schema = "public", joinColumns = @JoinColumn(name = "condition", referencedColumnName = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "adjustment", referencedColumnName = "id", nullable = false))
    public List<AdjustmentDefinitionJPA> getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(List<AdjustmentDefinitionJPA> adjustments) {
        this.adjustments = adjustments;
    }
}

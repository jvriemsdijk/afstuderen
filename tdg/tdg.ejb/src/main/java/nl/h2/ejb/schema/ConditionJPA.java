package nl.h2.ejb.schema;

import javax.persistence.*;
import java.util.List;

/**
 * Created by joeyvanriemsdijk on 02/06/16.
 */
@Entity
@Table(name = "conditions", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "Condition.findAll", query = "SELECT c FROM ConditionJPA c"),
        @NamedQuery(name = "Condition.deleteAll", query = "DELETE FROM ConditionJPA")
})
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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "adjustment_condition", catalog = "postgres", schema = "public", joinColumns = @JoinColumn(name = "condition", referencedColumnName = "id", nullable = false), inverseJoinColumns = @JoinColumn(name = "adjustment", referencedColumnName = "id", nullable = false))
    public List<AdjustmentDefinitionJPA> getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(List<AdjustmentDefinitionJPA> adjustments) {
        this.adjustments = adjustments;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConditionJPA that = (ConditionJPA) o;

        if (getId() != that.getId()) return false;
        if (isChronic() != that.isChronic()) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (isChronic() ? 1 : 0);
        return result;
    }
}

package nl.h2.ejb.schema;

import javax.persistence.*;
import java.util.List;

/**
 * Created by joeyvanriemsdijk on 02/06/16.
 */
@Entity
@Table(name = "person", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "Person.findAll", query = "SELECT a FROM PersonJPA a"),
        @NamedQuery(name = "Person.deleteAll", query = "DELETE FROM PersonJPA")
})
public class PersonJPA {
    private long bsn;
    private List<AdviceJPA> advice;
    private List<HousingSituationJPA> residence;
    private List<AdjustmentJPA> adjustmentHistory;

    @Id
    @Column(name = "bsn", nullable = false)
    public long getBsn() {
        return bsn;
    }

    public void setBsn(long bsn) {
        this.bsn = bsn;
    }

    @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL)
    public List<AdviceJPA> getAdvice() {
        return advice;
    }

    public void setAdvice(List<AdviceJPA> advice) {
        this.advice = advice;
    }

    @ManyToMany(mappedBy = "residents", cascade = CascadeType.ALL)
    public List<HousingSituationJPA> getResidence() {
        return residence;
    }

    public void setResidence(List<HousingSituationJPA> residence) {
        this.residence = residence;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "applicant_history", catalog = "postgres", schema = "public", joinColumns = @JoinColumn(name = "person", referencedColumnName = "bsn", nullable = false), inverseJoinColumns = @JoinColumn(name = "adjustment", referencedColumnName = "id", nullable = false))
    public List<AdjustmentJPA> getAdjustmentHistory() {
        return adjustmentHistory;
    }

    public void setAdjustmentHistory(List<AdjustmentJPA> adjustmentHistory) {
        this.adjustmentHistory = adjustmentHistory;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonJPA personJPA = (PersonJPA) o;

        if (getBsn() != personJPA.getBsn()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (getBsn() ^ (getBsn() >>> 32));
    }
}

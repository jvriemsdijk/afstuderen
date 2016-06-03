package nl.h2.ejb.schema;

import javax.persistence.*;
import java.util.List;

/**
 * Created by joeyvanriemsdijk on 02/06/16.
 */
@Entity
@Table(name = "advice", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "Advice.findAll", query = "SELECT a FROM AdviceJPA a"),
        @NamedQuery(name = "Advice.deleteAll", query = "DELETE FROM AdviceJPA")
})
public class AdviceJPA {
    private long id;
    private boolean goAhead;
    private PersonJPA applicant;
    private List<ConditionJPA> currentConditions;
    private List<ConditionJPA> futureConditions;
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
    @Column(name = "go_ahead", nullable = false)
    public boolean isGoAhead() {
        return goAhead;
    }

    public void setGoAhead(boolean goAhead) {
        this.goAhead = goAhead;
    }

    @ManyToOne
    @JoinColumn(name = "applicant", referencedColumnName = "bsn")
    public PersonJPA getApplicant() {
        return applicant;
    }

    public void setApplicant(PersonJPA applicant) {
        this.applicant = applicant;
    }

    @OneToMany
    public List<ConditionJPA> getCurrentConditions() {
        return currentConditions;
    }

    public void setCurrentConditions(List<ConditionJPA> currentConditions) {
        this.currentConditions = currentConditions;
    }

    @OneToMany
    public List<ConditionJPA> getFutureConditions() {
        return futureConditions;
    }

    public void setFutureConditions(List<ConditionJPA> futureConditions) {
        this.futureConditions = futureConditions;
    }

    @OneToOne(mappedBy = "advice")
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

        AdviceJPA adviceJPA = (AdviceJPA) o;

        if (getId() != adviceJPA.getId()) return false;
        if (isGoAhead() != adviceJPA.isGoAhead()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (isGoAhead() ? 1 : 0);
        return result;
    }
}

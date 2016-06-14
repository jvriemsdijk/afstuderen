package nl.h2.ejb.schema;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by: J. van Riemsdijk | H2
 */
@Entity
@Table(name = "advice", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "Advice.findAll", query = "SELECT a FROM AdviceJPA a")
})
public class AdviceJPA {

    private Long adviceId;
    private boolean goAhead;
    private List<ConditionJPA> currentConditions;
    private List<ConditionJPA> futureConditions;
    private WmoDecisionJPA decision;
    private Date dateIssued;
    private String remarks;
    private ApplicationJPA application;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advice_id", nullable = false)
    public Long getAdviceId() {
        return adviceId;
    }

    public void setAdviceId(Long adviceId) {
        this.adviceId = adviceId;
    }


    @Basic
    @Column(name = "date_issued")
    public Date getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }


    @Basic
    @Column(name = "remarks", length = -1)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    @OneToOne
    @JoinColumn(name = "application", referencedColumnName = "application_id", nullable = false)
    public ApplicationJPA getApplication() {
        return application;
    }

    public void setApplication(ApplicationJPA application) {
        this.application = application;
    }


    @Basic
    @Column(name = "go_ahead", nullable = false)
    public boolean isGoAhead() {
        return goAhead;
    }

    public void setGoAhead(boolean goAhead) {
        this.goAhead = goAhead;
    }


    @ManyToMany
    @JoinTable(name = "advice_to_current_condition", catalog = "postgres", schema = "public", joinColumns = @JoinColumn(name = "advice", referencedColumnName = "advice_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "condition", referencedColumnName = "condition_id", nullable = false))
    public List<ConditionJPA> getCurrentConditions() {
        return currentConditions;
    }

    public void setCurrentConditions(List<ConditionJPA> currentConditions) {
        this.currentConditions = currentConditions;
    }


    @ManyToMany
    @JoinTable(name = "advice_to_future_condition", catalog = "postgres", schema = "public", joinColumns = @JoinColumn(name = "advice", referencedColumnName = "advice_id", nullable = false), inverseJoinColumns = @JoinColumn(name = "condition", referencedColumnName = "condition_id", nullable = false))
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

        if (getAdviceId().equals(adviceJPA.getAdviceId())) return false;
        if (isGoAhead() != adviceJPA.isGoAhead()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getAdviceId() ^ (getAdviceId() >>> 32));
        result = 31 * result + (isGoAhead() ? 1 : 0);
        return result;
    }
}

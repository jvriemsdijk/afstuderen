package nl.h2.schema;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: J. van Riemsdijk | H2
 */
@Entity
@Table(name = "wmo_decision", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "WmoDecision.findAll", query = "SELECT a FROM WmoDecisionJPA a")
})
public class WmoDecisionJPA {

    private Long wmoDecisionId;
    private boolean granted;
    private String reason;
    private boolean exception;
    private AdviceJPA advice;
    private Date date;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wmo_decision_id", nullable = false)
    public Long getWmoDecisionId() {
        return wmoDecisionId;
    }

    public void setWmoDecisionId(Long wmoDecisionId) {
        this.wmoDecisionId = wmoDecisionId;
    }


    @Basic
    @Column(name = "granted", nullable = false)
    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }


    @Basic
    @Column(name = "reason", length = 1024)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    @Basic
    @Column(name = "exception", nullable = false)
    public boolean isException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "advice", referencedColumnName = "advice_id", nullable = false)
    public AdviceJPA getAdvice() {
        return advice;
    }

    public void setAdvice(AdviceJPA advice) {
        this.advice = advice;
    }


    @Basic
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WmoDecisionJPA that = (WmoDecisionJPA) o;

        if (getWmoDecisionId().equals(that.getWmoDecisionId())) return false;
        if (isGranted() != that.isGranted()) return false;
        if (isException() != that.isException()) return false;
        if (getReason() != null ? !getReason().equals(that.getReason()) : that.getReason() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 675;
        result = 31 * result + (isGranted() ? 1 : 0);
        result = 31 * result + (getReason() != null ? getReason().hashCode() : 0);
        result = 31 * result + (isException() ? 1 : 0);
        return result;
    }

}

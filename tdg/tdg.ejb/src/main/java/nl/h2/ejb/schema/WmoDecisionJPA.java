package nl.h2.ejb.schema;

import javax.persistence.*;
import java.util.List;

/**
 * Created by joeyvanriemsdijk on 03/06/16.
 */
@Entity
@Table(name = "wmo_decisions", schema = "public", catalog = "postgres")
@NamedQueries({
        @NamedQuery(name = "WmoDecision.findAll", query = "SELECT a FROM WmoDecisionJPA a"),
        @NamedQuery(name = "WmoDecision.deleteAll", query = "DELETE FROM WmoDecisionJPA")
})
public class WmoDecisionJPA {
    private long id;
    private boolean granted;
    private String reason;
    private boolean exception;
    private AdviceJPA advice;
    private List<AdjustmentJPA> adjustments;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
    @Column(name = "reason", nullable = true, length = 1024)
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
    @JoinColumn(name = "advice", referencedColumnName = "id", nullable = false)
    public AdviceJPA getAdvice() {
        return advice;
    }

    public void setAdvice(AdviceJPA advice) {
        this.advice = advice;
    }

    @OneToMany(mappedBy = "decision", cascade = CascadeType.ALL)
    public List<AdjustmentJPA> getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(List<AdjustmentJPA> adjustments) {
        this.adjustments = adjustments;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WmoDecisionJPA that = (WmoDecisionJPA) o;

        if (getId() != that.getId()) return false;
        if (isGranted() != that.isGranted()) return false;
        if (isException() != that.isException()) return false;
        if (getReason() != null ? !getReason().equals(that.getReason()) : that.getReason() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (getId() ^ (getId() >>> 32));
        result = 31 * result + (isGranted() ? 1 : 0);
        result = 31 * result + (getReason() != null ? getReason().hashCode() : 0);
        result = 31 * result + (isException() ? 1 : 0);
        return result;
    }
}

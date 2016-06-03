package nl.h2.ejb.schema;

import javax.persistence.*;
import java.util.List;

/**
 * Created by joeyvanriemsdijk on 03/06/16.
 */
@Entity
@Table(name = "wmo_decisions", schema = "public", catalog = "postgres")
public class WmoDecisionsJPA {
    private long id;
    private boolean granted;
    private String reason;
    private boolean exception;
    private AdviceJPA advice;
    private List<AdjustmentJPA> adjustments;

    @Id
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WmoDecisionsJPA that = (WmoDecisionsJPA) o;

        if (id != that.id) return false;
        if (granted != that.granted) return false;
        if (exception != that.exception) return false;
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (granted ? 1 : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (exception ? 1 : 0);
        return result;
    }

    @OneToOne
    @JoinColumn(name = "advice", referencedColumnName = "id", nullable = false)
    public AdviceJPA getAdvice() {
        return advice;
    }

    public void setAdvice(AdviceJPA advice) {
        this.advice = advice;
    }

    @OneToMany(mappedBy = "decision")
    public List<AdjustmentJPA> getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(List<AdjustmentJPA> adjustments) {
        this.adjustments = adjustments;
    }
}

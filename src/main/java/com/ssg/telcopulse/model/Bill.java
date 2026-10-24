package com.ssg.telcopulse.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter 
@Setter 
@NoArgsConstructor 
@Entity
public class Bill {

    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="bill_number", nullable=false,unique=true)
    private String billNumber;

    @Column(nullable=false)
    private BigDecimal amount;

    @Column(nullable=false, name="due_date")
    private LocalDate dueDate;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private BillStatus billStatus;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name="subscriber_id" , referencedColumnName = "id", nullable=false)
    private Subscriber subscriber;

    public Bill(String billNumber, BigDecimal amount, LocalDate dueDate, BillStatus billStatus) {
        this.billNumber = billNumber;
        this.amount = amount;
        this.dueDate = dueDate;
        this.billStatus = billStatus;
    }

    @Override
    public String toString() {
        return "Bill [id=" + id + ", billNumber=" + billNumber + ", amount=" + amount + ", dueDate=" + dueDate
                + ", billStatus=" + billStatus + ", subscriber=" + subscriber + "]";
    }




    
}

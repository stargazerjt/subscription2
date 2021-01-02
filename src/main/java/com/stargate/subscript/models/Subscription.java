package com.stargate.subscript.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="SUBSCRIPTION")
public class Subscription {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private int ID;
	@Column
	private Double subscriptAmt;
	@Column
	private String subscriptionType;
	@Column
	private int dayOfWeekMonthly;
	@Column
	private Date startDate;
	@Column
	private Date endDate;
	@Column
	private Date createdDate;
	@Column
	private Date lastUpdated;
	public int getID() {
		return ID;
	}
	public void setID(int ID) {
		this.ID = ID;
	}
	public Double getSubscriptAmt() {
		return subscriptAmt;
	}
	public void setSubscriptAmt(Double subscriptAmt) {
		this.subscriptAmt = subscriptAmt;
	}
	public String getSubscriptionType() {
		return subscriptionType;
	}
	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}
	public int getDayOfWeekMonthly() {
		return dayOfWeekMonthly;
	}
	public void setDayOfWeekMonthly(int dayOfWeekMonthly) {
		this.dayOfWeekMonthly = dayOfWeekMonthly;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Invoice> invLs = new ArrayList<>();
    
	public List<Invoice> getInvLs() {
		return invLs;
	}
	public void setInvLs(List<Invoice> invLs) {
		this.invLs = invLs;
	}	
    
    
}

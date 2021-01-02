package com.stargate.subscript.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="INVOICE")
public class Invoice {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private int ID;
//	@Column
//	private int subscriptId;
	@Column
	private Date invDate;
	@Column
	private Date createdDate;
	@Column
	private Date lastUpdated;
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
//	public int getSubscriptId() {
//		return subscriptId;
//	}
//	public void setSubscriptId(int subscriptId) {
//		this.subscriptId = subscriptId;
//	}
	public Date getInvDate() {
		return invDate;
	}
	public void setInvDate(Date invDate) {
		this.invDate = invDate;
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

    @ManyToOne
    private Subscription subscript;

	public Subscription getSubscript() {
		return subscript;
	}
	public void setSubscript(Subscription subscript) {
		this.subscript = subscript;
	}	
    
    
}

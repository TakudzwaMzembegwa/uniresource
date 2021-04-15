package com.uniresource.backend.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "location")
public class Location{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "location_id")
	private int id;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "country")
	private Country country;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "province", nullable = true)
	private Province province;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "university", nullable = true)
	private University university;

	public Location(Country country, Province province,University university)
	{
		this.country = country;
		this.province = province;
		this.university = university;
	}
}
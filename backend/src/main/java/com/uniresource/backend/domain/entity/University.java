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
@Data
@NoArgsConstructor
@Table(name = "university")
public class University {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "university_id")
	private int id;
	@Column(name = "university")
	private String name;
	@Column(name = "abbreviation")
	private String abbrev;// Abbreviation
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "province")
	private Province province;

	public University(String name, String abbrev, Province province) {
		this.name = name;
		this.abbrev = abbrev.toUpperCase();
		this.province = province;
	}

	public void setAbbrev(String abbreviation) {
		this.abbrev = abbreviation.toUpperCase();
	}
}
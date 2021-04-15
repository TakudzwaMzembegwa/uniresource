package com.uniresource.backend.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@Table(name = "province")
public class Province {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "province_id")
	private int id;
	@Column(name = "province", length = 50)
	private String name;
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "country")
	private Country country;
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@OneToMany(mappedBy = "province", fetch = FetchType.LAZY)
	private List<University> universities = new ArrayList<University>();

	public Province(String name, Country country) {
		this.name = name;
		this.country = country;
	}
}
package com.uniresource.backend.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@Entity
@Table(name = "country")
public class Country {
	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY)	@Column(name ="country_id")
	private int id;
	@Column(name = "country")
	private String name;
	@EqualsAndHashCode.Exclude @ToString.Exclude
	@OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
	private List<Province> provinces = new ArrayList<Province>();


	public Country(String name){
		this.name = name;
	}
}
package com.uniresource.backend.domain.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

	@Setter(AccessLevel.NONE)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NaturalId
	@Column(name = "username", nullable = false, unique = true, length = 30)
	private String username;

	private String fullname;
	@NaturalId
	@Column(name = "email", nullable = false, unique = true, length = 60)
	private String email;

	@Column(name = "phone_number", updatable = true, nullable = true, length = 18)
	private String phoneNumber;

	@Column(name = "password", updatable = true, nullable = false, length = 70)
	private String password;

	@Setter(AccessLevel.NONE)
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_joined", updatable = false, nullable = false)
	private Date dateJoined;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_updated", nullable = false)
	private Date lastUpdate;

	private String about;

	@Column(name = "profile_pic_location", unique = false/*true*/, length = 255)
	private String profilePic;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name = "location")
	@ToString.Exclude//-----------------------------
	private Location location;

	@Convert(converter = GenderAttributeConverter.class)
	private Gender gender;

	@Convert(converter = StudyYearAttributeConverter.class)
	@Column(name = "study_year")
	private StudyYear studyYear;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@ToString.Exclude
	List<Post> posts = new ArrayList<>();

	@Column(name = "total_Posts")
	private int totalPosts;

	@Column(name = "active_posts")
	private int activePosts;

	
	@Transient
    public static final String PREFIX = "user";

	public User(String username, String fullname, String email, String phoneNumber, String password,
			String about, String profilePic, Location location, Gender gender, StudyYear studyYear) {
		this.username = username;
		this.fullname = fullname;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.about = about;
		this.profilePic = profilePic;
		this.location = location;
		this.gender = gender;
		this.studyYear = studyYear;
		this.totalPosts = 0;
		this.activePosts = 0;
	}

	@JsonIgnore
	@JsonProperty(value = "password")
	public String getPassword() {
		return password;
	}

}
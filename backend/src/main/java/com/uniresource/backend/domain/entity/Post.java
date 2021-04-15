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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Transient;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@Table(name = "post")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private long id;

	private String title;

	private String description;

	private float price;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", updatable = false, nullable = false)
	private Date dateCreated;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_updated", nullable = false)
	private Date lastUpdate;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name = "location")
	@ToString.Exclude
	private Location location;

	@ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "user")
	@Transient
	private User user;

	@OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.MERGE})
	@Transient
	private List<PostImage> postImages = new ArrayList<PostImage>();

	@Convert(converter = CategoryAttributeConverter.class)
	private Category category;

	@Convert(converter = ConditionAttributeConverter.class)
	private Condition condition;
	
	@Convert(converter = PostStatusAttributeConverter.class)
	@Column(name = "post_status")
	private PostStatus postStatus;

	public Post(String title, String description, float price, User user,
			Location location, Category category, Condition condition, PostStatus postStatus) {

		this.title = title;
		this.description = description;
		this.price = price;
		this.user = user;
		this.location = location;
		this.category = category;
		this.condition = condition;
		this.postStatus = postStatus;
	}

}
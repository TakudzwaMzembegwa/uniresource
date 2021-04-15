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
import javax.persistence.Transient;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@Table(name = " post_images")
public class PostImage {
	@Id
	@Column(name = "post_image_id", updatable = false, nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "image", updatable = true, unique = true)
	private String image;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post")
	@ToString.Exclude
	private Post post;

	@Transient
    public static final String PREFIX = "post";

	public PostImage(String image, Post post) {
		this.post = post;
		this.image = image;
	}

}
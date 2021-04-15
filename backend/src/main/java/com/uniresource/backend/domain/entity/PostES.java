package com.uniresource.backend.domain.entity;

import java.time.Instant;

import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(indexName = "posts")
public class PostES {

	@Id
	@ReadOnlyProperty
	private String id;

	@Field(name = "post_id", type = FieldType.Long)
	private long postId;

	@Field(type = FieldType.Text)
	private String title;

	@Field(type = FieldType.Text)// save it as pure text not tokens used for fast searches or maybe not could be useful if results are few (index = false/true)
    private String description;

	@Field(type = FieldType.Float)
	private float price;

	@CreatedDate
	@Field(name = "date_created", type = FieldType.Date, format = DateFormat.date_time)
	private Instant dateCreated;

	@LastModifiedDate
	@Field(name = "last_updated", type = FieldType.Date, format = DateFormat.date_time)
	private Instant lastUpdate;

	/*//?
	@ToString.Exclude
	//@Field(name = "location", type = FieldType.)
	private Location location;*/
	@Field(type = FieldType.Keyword)
    private String country;

	@Field(type = FieldType.Keyword)
    private String province;

	@Field(type = FieldType.Keyword)
    private String university;

	@Field(name = "post_image",type = FieldType.Text, index = false)
	private String postImage;

	@Field(type = FieldType.Keyword)
	private Category category;

	@Field(type = FieldType.Keyword)
	private Condition condition;

	@Field(name = "post_status", type = FieldType.Keyword)
	private PostStatus postStatus;

	public PostES(long postId, String title, String description, float price, String country, String province,
			String university, String postImage, Category category, Condition condition, PostStatus postStatus) {
		this.postId = postId;
		this.title = title;
		this.description = description;
		this.price = price;
		this.country = country;
		this.province = province;
		this.university = university;
		this.postImage = postImage;
		this.category = category;
		this.condition = condition;
		this.postStatus = postStatus;
	}

	public PostES(Post post) {
		this.postId = post.getId();
		this.title = post.getTitle();
		this.description = post.getDescription();
		this.price = post.getPrice();
		this.country = post.getLocation().getCountry().getName();
		this.province = post.getLocation().getProvince().getName();
		this.university = post.getLocation().getUniversity().getName();
		if (post.getPostImages().size() > 0)
			this.postImage = post.getPostImages().get(0).getImage();
		else
			this.postImage = "noimage.png";
		this.category = post.getCategory();
		this.condition = post.getCondition();
		this.postStatus = post.getPostStatus();
	}

}

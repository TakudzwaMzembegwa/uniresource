package com.uniresource.backend.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import com.uniresource.backend.domain.dto.CreatePostRequest;
import com.uniresource.backend.domain.dto.PostDto;
import com.uniresource.backend.domain.dto.PostSearchFilter;
import com.uniresource.backend.domain.dto.PostSearchRequest;
import com.uniresource.backend.domain.dto.PostSummary;
import com.uniresource.backend.domain.dto.UpdatePostRequest;
import com.uniresource.backend.domain.entity.Category;
import com.uniresource.backend.domain.entity.Condition;
import com.uniresource.backend.domain.entity.Post;
import com.uniresource.backend.domain.entity.PostES;
import com.uniresource.backend.domain.entity.PostImage;
import com.uniresource.backend.domain.entity.PostStatus;
import com.uniresource.backend.domain.entity.User;
import com.uniresource.backend.domain.mapper.LocationMapper;
import com.uniresource.backend.domain.mapper.PostMapper;
import com.uniresource.backend.repository.LocationRepository;
import com.uniresource.backend.repository.PostElasticsearchRepository;
import com.uniresource.backend.repository.PostImageRepository;
import com.uniresource.backend.repository.PostRepository;
import com.uniresource.backend.repository.UserRepository;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    public static final Logger log = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private RestHighLevelClient highLevelClient;

    @Autowired
    private PostElasticsearchRepository postESRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostImageRepository postImageRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private PostImageService postImageService;
    @Autowired
    private UserService userService;

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private LocationMapper locationMapper;

    public static final String INDEX = "posts";
    // public PostStatus postStatus; check(in service) before sending to client
    // add directory to user when sending image to client

    // In elastic search you could use location attributes` '*SQL' ids to save save
    // space and to reduce data transferred on the net

    @Transactional
    public PostDto save(MultipartFile[] files, CreatePostRequest request, String username) {
        Post post = postMapper.createPost(request);
        post.setPostImages(
                Arrays.asList(files).stream().map(f -> postImageService.create(f)).collect(Collectors.toList()));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exists: " + username));
        user.setTotalPosts(user.getTotalPosts() + 1);
        post.setPostStatus(PostStatus.ACTIVE);
        user.setActivePosts(user.getActivePosts() + 1);
        post.setUser(user);
        userRepository.save(user);
        post = postRepository.save(post);
        for(PostImage image: post.getPostImages()){
            image.setPost(post);
        }
        postImageRepository.saveAll(post.getPostImages());
        // Save to ElasticSearch after the post is activated
        postESRepository.save(new PostES(post));
        return postMapper.toPostDto(post);
    }

    @Transactional
    public void update(MultipartFile[] files, UpdatePostRequest updatePost) throws IOException {
        Post postToUpdate = postRepository.getOne(updatePost.getPostId());
        Map<String, Object> params = new HashMap<>();

        if (!updatePost.getTitle().isBlank()) {
            postToUpdate.setTitle(updatePost.getTitle());
            params.put("title", updatePost.getTitle());
        }
        if (!updatePost.getDescription().isBlank()) {
            postToUpdate.setDescription(updatePost.getDescription());
            params.put("description", updatePost.getDescription());
        }
        if (updatePost.getPrice() > 0) {
            postToUpdate.setPrice(updatePost.getPrice());
            params.put("price", updatePost.getPrice());
        }
        if (updatePost.getLocation() != null && updatePost.getLocation().getLocationId() > 0L) {
            postToUpdate.setLocation(locationRepository.getOne(updatePost.getLocation().getLocationId()));
            params.put("country", updatePost.getLocation().getCountry());
            params.put("province", updatePost.getLocation().getProvince());
            params.put("university", updatePost.getLocation().getUniversity());
        }
        if (updatePost.getPostImages() != null) {
            List<PostImage> postImages = postImageService
            .save(files,
             updatePost.getPostImages(), postToUpdate);
            params.put("post_image", FileStorageService.THUMBNAIL_PREFIX + postImages.get(0).getImage());

        }
        if (!updatePost.getCondition().isBlank()) {
            postToUpdate.setCondition(Condition.valueOf(updatePost.getCondition().toUpperCase()));
            params.put("condition", Condition.valueOf(updatePost.getCondition().toUpperCase()));
        }
        if (!updatePost.getCategory().isBlank()) {
            postToUpdate.setCategory(Category.valueOf(updatePost.getCategory().toUpperCase()));
            params.put("category", Category.valueOf(updatePost.getCategory().toUpperCase()));
        }

        UpdateByQueryRequest updateRequest = new UpdateByQueryRequest(INDEX);
        updateRequest.setQuery(QueryBuilders.termQuery("post_id", updatePost.getPostId()));
        updateRequest.setConflicts("proceed");
        Script updateScript = new Script(ScriptType.INLINE, "painless", "ctx._source.putAll(params)", params);
        updateRequest.setScript(updateScript);
        postRepository.save(postToUpdate);
        highLevelClient.updateByQuery(updateRequest, RequestOptions.DEFAULT);
    }

    @Transactional
    public void delete(long id) {
        Post post = postRepository.findById(id).orElseThrow();
        post.getPostImages().forEach(im -> postImageService.delete(im));
        postRepository.delete(post);
        postESRepository.deleteByPostId(id);
    }

    @Transactional
    public PostDto getPostDto(long id) {
       Post post = postRepository.findById(id).orElseThrow();
        return postMapper.toPostDto(post);
    }

    @Transactional
    public List<PostSummary> search(PostSearchRequest postSearch) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!postSearch.getCategory().isBlank()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("category", postSearch.getCategory()));
        }
        if (!postSearch.getCondition().isBlank()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("condition", postSearch.getCondition()));
        }
        if (!postSearch.getCountry().isBlank()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("country", postSearch.getCountry()));
        }
        if (!postSearch.getProvince().isBlank()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("province", postSearch.getProvince()));
        }
        if (!postSearch.getUniversity().isBlank()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("university", postSearch.getUniversity()));
        }
        if (!postSearch.getTitle().isBlank()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("title", "" + postSearch.getTitle()));
        }
        if (postSearch.getPriceTo() > 0 || postSearch.getPriceFrom() > 0) {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("price")
            .from(postSearch.getPriceFrom())
            .to(postSearch.getPriceTo() <= 0 ? Integer.MAX_VALUE : postSearch.getPriceTo()));
        }
        
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                .withSort(new ScoreSortBuilder().order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("date_created").order(SortOrder.DESC))
                .withPageable(PageRequest.of(postSearch.getPageNumber(), postSearch.getPageSize())).build();

        SearchHits<PostES> posts = elasticsearchRestTemplate.search(searchQuery, PostES.class,
                IndexCoordinates.of(INDEX));

        return posts.stream().map(p -> postMapper.toPostSummary(p.getContent())).collect(Collectors.toList());
    }

    /*
     * public List<PostDto> filter(Page page, Query query){
     * 
     * }
     */
}

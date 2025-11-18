package pl.fishingwear.blog.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.fishingwear.blog.dto.PostDto;
import pl.fishingwear.blog.dto.PostSideBarDto;
import pl.fishingwear.blog.mapper.PostMapper;
import pl.fishingwear.blog.mapper.PostSideBarMapper;
import pl.fishingwear.blog.model.Post;
import pl.fishingwear.blog.model.enums.PostStatus;
import pl.fishingwear.blog.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BlogService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public Page<PostDto> getPosts(int page, int size, String search, Long categoryId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage;

        if (search == null || search.trim().isEmpty()) {

            search = "";
        }

        postPage = postRepository.searchPosts(search, categoryId, pageable);

        return postPage.map(postMapper::toDto);
    }

    private PostDto convertPostToDto(Post post){
        return postMapper.toDto(post);
    }

    public List<PostSideBarDto> getTop3Post(){
        return postRepository.findTop3ByStatusOrderByCreatedAtDesc(PostStatus.PUBLISHED)
                .stream()
                .map(PostSideBarMapper::toDto)
                .collect(Collectors.toList());
    }
}

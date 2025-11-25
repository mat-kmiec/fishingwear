package pl.fishingwear.blog.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import pl.fishingwear.blog.dto.PostDetailsDto;
import pl.fishingwear.blog.dto.PostDto;
import pl.fishingwear.blog.dto.PostSideBarDto;
import pl.fishingwear.blog.exception.PostNotFoundException;
import pl.fishingwear.blog.mapper.PostDetailsMapper;
import pl.fishingwear.blog.mapper.PostMapper;
import pl.fishingwear.blog.mapper.PostSideBarMapper;
import pl.fishingwear.blog.model.Comment;
import pl.fishingwear.blog.model.Post;
import pl.fishingwear.blog.model.enums.CommentStatus;
import pl.fishingwear.blog.model.enums.PostStatus;
import pl.fishingwear.blog.repository.CommentRepository;
import pl.fishingwear.blog.repository.PostRepository;
import pl.fishingwear.common.exception.UserNotFoundException;
import pl.fishingwear.user.model.User;
import pl.fishingwear.user.repository.UserRepository;
import pl.fishingwear.user.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BlogService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final PostDetailsMapper postDetailsMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public Page<PostDto> getPosts(int page, int size, String search, Long categoryId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage;

        if (search == null || search.trim().isEmpty()) {
            search = "";
        }
        postPage = postRepository.searchPosts(search, categoryId, pageable);
        return postPage.map(postMapper::toDto);
    }

    public List<PostSideBarDto> getTop3Post(){
        return postRepository.findTop3ByStatusOrderByCreatedAtDesc(PostStatus.PUBLISHED)
                .stream()
                .map(PostSideBarMapper::toDto)
                .collect(Collectors.toList());
    }

    public PostDetailsDto getPostById(Long id) {
        Post post = postRepository.findByIdAndStatus(id, PostStatus.PUBLISHED)
                .orElseThrow(PostNotFoundException::new);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAnonymous = (auth == null || !auth.isAuthenticated() ||
                auth instanceof AnonymousAuthenticationToken);

        List<Comment> comments;

        if (isAnonymous) {
            comments = commentRepository.findByPostIdAndStatus(post.getId(), CommentStatus.APPROVED);
        } else {
            boolean isStaff = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals("ROLE_ADMIN") || role.equals("ROLE_MODERATOR"));

            if (isStaff) {
                comments = commentRepository.findByPostId(post.getId());
            } else {
                User user = userService.getCurrentUser().orElseThrow(UserNotFoundException::new);
                comments = commentRepository.findVisibleForUser(post.getId(), user.getId());
            }
        }
        return postDetailsMapper.toDto(post, comments);
    }

//    private User getCurrentUrer(Authentication auth) {
//        Object principal = auth.getPrincipal();
//        String email;
//
//        if (principal instanceof OAuth2User) {
//            OAuth2User oauthUser = (OAuth2User) principal;
//            email = oauthUser.getAttribute("email");
//        } else if (principal instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) principal;
//            email = userDetails.getUsername();
//        } else {
//            throw new IllegalStateException("Nieobsługiwany typ użytkownika: " + principal.getClass());
//        }
//
//        return userRepository.findByEmail(email)
//                .orElseThrow(UserNotFoundException::new);
//    }

}

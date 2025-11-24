package pl.fishingwear.blog.model;

import jakarta.persistence.*;
import lombok.*;
import pl.fishingwear.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blog_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    private BlogCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<BlogCategory> subCategories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id")
    private User assignedModerator;

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<Post> posts;
}


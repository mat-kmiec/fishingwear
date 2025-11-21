package pl.fishingwear.user.model;

import jakarta.persistence.*;
import lombok.*;
import pl.fishingwear.auth.model.AuthProvider;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;
    private String phoneNumber;
    private Boolean enabled = true;


    @Column(name = "reputation_points")
    @Builder.Default
    private int reputationPoints = 0;

    @Column(name = "rank_name")
    @Builder.Default
    private String rankName = "Nowicjusz";

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthProvider authProvider;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();


    public void updateRank() {
        if (this.reputationPoints >= 100) this.rankName = "Złoty Wędkarz";
        else if (this.reputationPoints >= 50) this.rankName = "Srebrny Wędkarz";
        else this.rankName = "Nowicjusz";
    }

    public String getRankName(){
        updateRank();
        return rankName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

}

package pl.fishingwear.slider.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "slider_item")
@Getter
@Setter
public class SliderItem {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slider_id")
    private Slider slider;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "slider_image_id")
    private SliderImage image;

}

package pl.fishingwear.slider.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "slider")
public class Slider {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "slider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SliderItem> items = new ArrayList<>();

    public void addItem(SliderItem item) {
        items.add(item);
        item.setSlider(this);
    }

    public void removeItem(SliderItem item) {
        items.remove(item);
        item.setSlider(null);
    }


}

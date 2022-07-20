package com.tutti.backend.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class LiveRoom extends Timestamped{

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String roomTitle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String thumbnailImageUrl;

    @Column(nullable = false)
    private boolean onAir;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy="LiveRoom",orphanRemoval = true)
    private List<LiveRoomMessage> messages = new ArrayList<>();



    public LiveRoom(String roomTitle, User user, String description, String thumbnailImageUrl) {
        this.roomTitle = roomTitle;
        this.user = user;
        this.description = description;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.onAir = true;
    }
}

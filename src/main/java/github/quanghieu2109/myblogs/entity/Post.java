package github.quanghieu2109.myblogs.entity;

import github.quanghieu2109.myblogs.enums.PostStatus;
import github.quanghieu2109.myblogs.enums.PostVisibilities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne
    User author;
    String content;
    @Enumerated(EnumType.STRING)
    PostStatus status;
    @Enumerated(EnumType.STRING)
    PostVisibilities visibility;
    @OneToMany
    List<Media> medias;
    @OneToMany
    List<Comment> comments;



}

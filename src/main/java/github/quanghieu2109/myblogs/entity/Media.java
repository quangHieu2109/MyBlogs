package github.quanghieu2109.myblogs.entity;

import github.quanghieu2109.myblogs.enums.MediaStatus;
import github.quanghieu2109.myblogs.enums.MediaTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @ManyToOne
    @JoinColumn(name = "post_id")
    Post post;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    Comment comment;
    @Enumerated(EnumType.STRING)
    MediaStatus status;
    @Enumerated(EnumType.STRING)
    MediaTypes type; // IMAGE | VIDEO | OTHER
    String url; // public or signed URL
//    image/jpg, video/mp4
    String mime;
    Long size;
    Integer width;
    Integer height;
    String thumbUrl;
    Long duration = 0L; // ms for video
    Instant createdAt;
}

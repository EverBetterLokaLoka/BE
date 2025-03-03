package com.example.lokaloka.controller;

import com.example.lokaloka.domain.dto.reqdto.CommentReqDTO;
import com.example.lokaloka.domain.dto.reqdto.LikeReqDTO;
import com.example.lokaloka.domain.dto.reqdto.PostReqDTO;
import com.example.lokaloka.service.ICommentService;
import com.example.lokaloka.service.ILikeService;
import com.example.lokaloka.service.IPostService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// PostController.java
@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostRestController {
    private final IPostService postService;
    private final ICommentService commentService;
    private final ILikeService likeService;

    //done
    @PostMapping
    public ResponseEntity<PostReqDTO> createPost(@RequestBody PostReqDTO postReqDTO) {
        return ResponseEntity.ok(postService.createPost(postReqDTO));
    }
    //done
    @PutMapping("/{id}")
    public ResponseEntity<PostReqDTO> updatePost(@PathVariable Long id, @RequestBody PostReqDTO postReqDTO) {
        return ResponseEntity.ok(postService.updatePost(id, postReqDTO));
    }
    //done
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
    //done
    @GetMapping
    public ResponseEntity<List<PostReqDTO>> getUserPosts() {
        return ResponseEntity.ok(postService.getUserPosts());
    }

    //done
    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentReqDTO> addComment(@PathVariable Long postId, @RequestBody CommentReqDTO commentDTO) {
        commentDTO.setPostId(postId);
        return ResponseEntity.ok(commentService.createComment(commentDTO));
    }

    @GetMapping("{postId}/comments")
    public ResponseEntity<List<CommentReqDTO>> getAllComments(@PathVariable Long postId) {
        List<CommentReqDTO> comments = commentService.getAllComment(postId);
        return ResponseEntity.ok(comments);
    }
    //update comment
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentReqDTO> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentReqDTO commentDTO) {
        commentDTO.setPostId(postId);
        return ResponseEntity.ok(commentService.updateComment(commentId,commentDTO));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    //done
    @PostMapping("/{postId}/likes")
    public ResponseEntity<LikeReqDTO> toggleLike(@PathVariable Long postId) {
        return ResponseEntity.ok(likeService.toggleLike(postId));
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<List<LikeReqDTO>> getAllLikes(@PathVariable Long postId) {
        List<LikeReqDTO> likeList = likeService.getAllLikeForPost(postId);
        return ResponseEntity.ok(likeList);
    }
}

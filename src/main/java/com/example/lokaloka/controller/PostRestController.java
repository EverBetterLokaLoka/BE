package com.example.lokaloka.controller;

import com.example.lokaloka.domain.dto.reqdto.CommentReqDTO;
import com.example.lokaloka.domain.dto.reqdto.LikeReqDTO;
import com.example.lokaloka.domain.dto.reqdto.PostReqDTO;
import com.example.lokaloka.service.ICommentService;
import com.example.lokaloka.service.ILikeService;
import com.example.lokaloka.service.IPostService;
import com.example.lokaloka.util.ApiResponse;
import com.example.lokaloka.util.ResponseData;
import com.example.lokaloka.util.SuccessCode;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<?> createPost(@RequestBody PostReqDTO postReqDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseData.builder()
                        .success(true)
                        .status(HttpStatus.CREATED.value())
                        .message(SuccessCode.CREATED.getMessage())
                        .data(postService.createPost(postReqDTO))
                        .build());
    }
    //done

    @PutMapping("/{id}")
    public ResponseEntity<PostReqDTO> updatePost(@PathVariable Long id, @RequestBody PostReqDTO postReqDTO) {
        return ResponseEntity.ok(postService.updatePost(id, postReqDTO));
    }
    //done
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {

        return postService.deletePost(id);
    }
    //done
    @GetMapping
    public ResponseEntity<List<PostReqDTO>> getUserPosts() {
        return ResponseEntity.ok(postService.getUserPosts());
    }

    //done
    @PostMapping("/{postId}/comments")
    public ResponseEntity<?> addComment( @PathVariable Long postId,@Valid @RequestBody CommentReqDTO commentDTO, BindingResult result) {
        if (result.hasErrors()) {
            // Lấy lỗi đầu tiên
            String errorMessage = result.getFieldErrors().get(0).getDefaultMessage();

            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .success(false)
                            .status(HttpStatus.BAD_REQUEST.value()) // Đúng status
                            .message(errorMessage) // Chỉ lấy lỗi đầu tiên
                            .build()
            );
        }
        commentDTO.setPostId(postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseData.builder()
                        .success(true)
                        .status(HttpStatus.CREATED.value())
                        .message(SuccessCode.GET_SUCCESSFUL.getMessage())
                        .data(commentService.createComment(commentDTO))
                        .build());
    }

    @GetMapping("{postId}/comments")
    public ResponseEntity<List<CommentReqDTO>> getAllComments(@PathVariable Long postId) {
        List<CommentReqDTO> comments = commentService.getAllComment(postId);
        return ResponseEntity.ok(comments);
    }
    //update comment
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long postId, @PathVariable Long commentId,@Valid @RequestBody CommentReqDTO commentDTO,BindingResult result) {
        if (result.hasErrors()) {
            // Lấy lỗi đầu tiên
            String errorMessage = result.getFieldErrors().get(0).getDefaultMessage();

            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .success(false)
                            .status(HttpStatus.BAD_REQUEST.value()) // Đúng status
                            .message(errorMessage) // Chỉ lấy lỗi đầu tiên
                            .build()
            );
        }
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
    public ResponseEntity<?> toggleLike(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseData.builder()
                        .success(true)
                        .status(HttpStatus.CREATED.value())
                        .data(likeService.toggleLike(postId))
                        .build()
        );
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<List<LikeReqDTO>> getAllLikes(@PathVariable Long postId) {
        List<LikeReqDTO> likeList = likeService.getAllLikeForPost(postId);
        return ResponseEntity.ok(likeList);
    }

    // Lấy tất cả bài viết trong hệ thống với is_destroyed = false
    @GetMapping("/all")
    public ResponseEntity<List<PostReqDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

}

package com.sbackjung.transferstay.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Like_Accommodation")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LikeAccommodation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long likeId;

  /**
   * // User와의 Many-to-One 관계
   *
   * @ManyToOne(fetch = FetchType.LAZY)
   * @JoinColumn(name = "user_id", nullable = false) private User user; // todo : 유저
   */

  @Column(name = "user_id", nullable = false)
  private Long userId;

  // Post와의 Many-to-One 관계
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private AssignmentPost assignmentPost;

}

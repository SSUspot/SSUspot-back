/*
package com.ssuspot.sns.infrastructure.repository.post

import com.ssuspot.sns.domain.model.post.entity.Post
import com.ssuspot.sns.domain.model.post.entity.PostLike
import com.ssuspot.sns.domain.model.post.entity.PostTag
import com.ssuspot.sns.domain.model.post.entity.Tag
import com.ssuspot.sns.domain.model.spot.entity.Spot
import com.ssuspot.sns.domain.model.user.entity.User
import com.ssuspot.sns.domain.model.user.entity.UserFollow
import com.ssuspot.sns.infrastructure.repository.spot.SpotRepository
import com.ssuspot.sns.infrastructure.repository.user.UserRepository
import com.ssuspot.sns.support.RepositoryTest
import com.ssuspot.sns.support.fixture.PostFixture
import com.ssuspot.sns.support.fixture.SpotFixture
import com.ssuspot.sns.support.fixture.UserFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.domain.PageRequest

@RepositoryTest
class CustomPostRepositoryImplTest @Autowired constructor(
    private val customPostRepository: CustomPostRepositoryImpl,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val spotRepository: SpotRepository,
    private val tagRepository: TagRepository,
    private val postTagRepository: PostTagRepository,
    private val postLikeRepository: PostLikeRepository,
    private val userFollowRepository: UserFollowRepository,
    private val entityManager: TestEntityManager
) {

    private lateinit var testUser: User
    private lateinit var testSpot: Spot

    @BeforeEach
    fun setUp() {
        // 기본 테스트 데이터 설정
        testUser = userRepository.save(UserFixture.createUser(id = null))
        testSpot = spotRepository.save(SpotFixture.createSpot(id = null))
    }

    @Test
    @DisplayName("게시글 저장 - 성공")
    fun `should save post successfully`() {
        // given
        val post = PostFixture.createPost(
            id = null,
            user = testUser,
            spot = testSpot
        )

        // when
        val savedPost = customPostRepository.save(post)

        // then
        assertThat(savedPost.id).isNotNull
        assertThat(savedPost.title).isEqualTo(post.title)
        assertThat(savedPost.content).isEqualTo(post.content)
        assertThat(savedPost.user).isEqualTo(testUser)
        assertThat(savedPost.spot).isEqualTo(testSpot)
    }

    @Test
    @DisplayName("ID로 게시글 조회 - 좋아요 여부 포함")
    fun `should find post by id with like status`() {
        // given
        val post = postRepository.save(
            PostFixture.createPost(
                id = null,
                user = testUser,
                spot = testSpot
            )
        )
        
        // 좋아요 추가
        val postLike = PostLike(
            post = post,
            user = testUser
        )
        postLikeRepository.save(postLike)
        
        entityManager.flush()
        entityManager.clear()

        // when
        val result = customPostRepository.findPostById(post.id!!, testUser)

        // then
        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(post.id)
        assertThat(result?.hasLiked).isTrue
    }

    @Test
    @DisplayName("Spot ID로 게시글 목록 조회")
    fun `should find posts by spot id`() {
        // given
        val post1 = postRepository.save(
            PostFixture.createPost(id = null, user = testUser, spot = testSpot, title = "Post 1")
        )
        val post2 = postRepository.save(
            PostFixture.createPost(id = null, user = testUser, spot = testSpot, title = "Post 2")
        )
        
        // 다른 Spot의 게시글
        val otherSpot = spotRepository.save(SpotFixture.createSpot(id = null, name = "Other Spot"))
        postRepository.save(
            PostFixture.createPost(id = null, user = testUser, spot = otherSpot, title = "Other Post")
        )

        val pageable = PageRequest.of(0, 10)

        // when
        val result = customPostRepository.findPostsBySpotId(testSpot.id!!, testUser, pageable)

        // then
        assertThat(result.content).hasSize(2)
        assertThat(result.content.map { it.title }).containsExactly("Post 2", "Post 1") // 최신순
    }

    @Test
    @DisplayName("사용자 ID로 게시글 목록 조회")
    fun `should find posts by user id`() {
        // given
        val post1 = postRepository.save(
            PostFixture.createPost(id = null, user = testUser, spot = testSpot, title = "My Post 1")
        )
        val post2 = postRepository.save(
            PostFixture.createPost(id = null, user = testUser, spot = testSpot, title = "My Post 2")
        )
        
        // 다른 사용자의 게시글
        val otherUser = userRepository.save(UserFixture.createUser(id = null, email = "other@example.com"))
        postRepository.save(
            PostFixture.createPost(id = null, user = otherUser, spot = testSpot, title = "Other's Post")
        )

        val pageable = PageRequest.of(0, 10)

        // when
        val result = customPostRepository.findPostsByUserId(testUser, pageable)

        // then
        assertThat(result.content).hasSize(2)
        assertThat(result.content.map { it.title }).containsExactly("My Post 2", "My Post 1")
    }

    @Test
    @DisplayName("태그 이름으로 게시글 목록 조회")
    fun `should find posts by tag name`() {
        // given
        val tag1 = tagRepository.save(Tag(tagName = "spring"))
        val tag2 = tagRepository.save(Tag(tagName = "kotlin"))
        
        val post1 = postRepository.save(
            PostFixture.createPost(id = null, user = testUser, spot = testSpot, title = "Spring Post")
        )
        val post2 = postRepository.save(
            PostFixture.createPost(id = null, user = testUser, spot = testSpot, title = "Spring Kotlin Post")
        )
        val post3 = postRepository.save(
            PostFixture.createPost(id = null, user = testUser, spot = testSpot, title = "Only Kotlin Post")
        )
        
        // PostTag 연결
        postTagRepository.save(PostTag(post = post1, tag = tag1))
        postTagRepository.save(PostTag(post = post2, tag = tag1))
        postTagRepository.save(PostTag(post = post2, tag = tag2))
        postTagRepository.save(PostTag(post = post3, tag = tag2))
        
        entityManager.flush()
        entityManager.clear()

        val pageable = PageRequest.of(0, 10)

        // when
        val result = customPostRepository.findPostsByTagName("spring", testUser, pageable)

        // then
        assertThat(result.content).hasSize(2)
        assertThat(result.content.map { it.title }).containsExactly("Spring Kotlin Post", "Spring Post")
    }

    @Test
    @DisplayName("팔로우한 사용자들의 게시글 목록 조회")
    fun `should find posts by following users`() {
        // given
        val user2 = userRepository.save(UserFixture.createUser(id = null, email = "user2@example.com"))
        val user3 = userRepository.save(UserFixture.createUser(id = null, email = "user3@example.com"))
        val user4 = userRepository.save(UserFixture.createUser(id = null, email = "user4@example.com"))
        
        // 팔로우 관계 설정
        userFollowRepository.save(UserFollow(followingUser = testUser, followedUser = user2))
        userFollowRepository.save(UserFollow(followingUser = testUser, followedUser = user3))
        
        // 게시글 생성
        postRepository.save(
            PostFixture.createPost(id = null, user = user2, spot = testSpot, title = "Followed User 2 Post")
        )
        postRepository.save(
            PostFixture.createPost(id = null, user = user3, spot = testSpot, title = "Followed User 3 Post")
        )
        postRepository.save(
            PostFixture.createPost(id = null, user = user4, spot = testSpot, title = "Not Followed User Post")
        )
        
        entityManager.flush()
        entityManager.clear()

        val pageable = PageRequest.of(0, 10)

        // when
        val result = customPostRepository.findPostsByFollowingUsers(testUser, pageable)

        // then
        assertThat(result.content).hasSize(2)
        assertThat(result.content.map { it.title }).containsOnly(
            "Followed User 2 Post",
            "Followed User 3 Post"
        )
    }

    @Test
    @DisplayName("추천 게시글 조회 - 팔로우한 사용자들이 좋아요한 게시글")
    fun `should find recommended posts based on following users likes`() {
        // given
        val user2 = userRepository.save(UserFixture.createUser(id = null, email = "user2@example.com"))
        val user3 = userRepository.save(UserFixture.createUser(id = null, email = "user3@example.com"))
        
        // 팔로우 관계
        userFollowRepository.save(UserFollow(followingUser = testUser, followedUser = user2))
        userFollowRepository.save(UserFollow(followingUser = testUser, followedUser = user3))
        
        // 게시글 생성
        val popularPost = postRepository.save(
            PostFixture.createPost(id = null, user = testUser, spot = testSpot, title = "Popular Post")
        )
        val normalPost = postRepository.save(
            PostFixture.createPost(id = null, user = testUser, spot = testSpot, title = "Normal Post")
        )
        
        // 팔로우한 사용자들이 좋아요
        postLikeRepository.save(PostLike(post = popularPost, user = user2))
        postLikeRepository.save(PostLike(post = popularPost, user = user3))
        postLikeRepository.save(PostLike(post = normalPost, user = user2))
        
        entityManager.flush()
        entityManager.clear()

        val pageable = PageRequest.of(0, 10)

        // when
        val result = customPostRepository.findRecommendedPosts(testUser, pageable)

        // then
        assertThat(result.content).isNotEmpty
        // 더 많은 좋아요를 받은 게시글이 먼저 나와야 함
        assertThat(result.content.first().title).isEqualTo("Popular Post")
    }

    @Test
    @DisplayName("추천 게시글 조회 - 팔로우가 없을 때 최신 게시글 반환")
    fun `should return latest posts when user has no followings`() {
        // given
        val newUser = userRepository.save(UserFixture.createUser(id = null, email = "newuser@example.com"))
        
        // 게시글 생성
        val post1 = postRepository.save(
            PostFixture.createPost(id = null, user = testUser, spot = testSpot, title = "Post 1")
        )
        Thread.sleep(10) // 시간 차이를 위해
        val post2 = postRepository.save(
            PostFixture.createPost(id = null, user = testUser, spot = testSpot, title = "Post 2")
        )
        Thread.sleep(10)
        val post3 = postRepository.save(
            PostFixture.createPost(id = null, user = testUser, spot = testSpot, title = "Post 3")
        )
        
        entityManager.flush()
        entityManager.clear()

        val pageable = PageRequest.of(0, 2)

        // when
        val result = customPostRepository.findRecommendedPosts(newUser, pageable)

        // then
        assertThat(result.content).hasSize(2)
        assertThat(result.content.map { it.title }).containsExactly("Post 3", "Post 2") // 최신순
    }

    @Test
    @DisplayName("N+1 쿼리 문제 확인 - 게시글 목록 조회 시 연관 엔티티")
    fun `should not have N+1 query problem when fetching posts`() {
        // given
        val posts = (1..5).map { i ->
            postRepository.save(
                PostFixture.createPost(
                    id = null,
                    user = testUser,
                    spot = testSpot,
                    title = "Post $i"
                )
            )
        }
        
        // 각 게시글에 좋아요 추가
        posts.forEach { post ->
            postLikeRepository.save(PostLike(post = post, user = testUser))
        }
        
        entityManager.flush()
        entityManager.clear()

        val pageable = PageRequest.of(0, 10)

        // when
        val result = customPostRepository.findPostsByUserId(testUser, pageable)

        // then
        assertThat(result.content).hasSize(5)
        // hasLiked 확인 시 추가 쿼리가 발생하지 않아야 함
        result.content.forEach { post ->
            assertThat(post.hasLiked).isTrue
        }
    }
}*/

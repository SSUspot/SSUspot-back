-- ================================================
-- Database Indexes for Performance Optimization
-- Created for Phase 3: Performance Optimization
-- ================================================

-- Users table indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_users_nickname ON users(nickname);

-- Posts table indexes  
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_posts_user_id ON posts(user_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_posts_spot_id ON posts(spot_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_posts_created_at ON posts(created_at DESC);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_posts_user_created ON posts(user_id, created_at DESC);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_posts_spot_created ON posts(spot_id, created_at DESC);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_posts_like_count ON posts(like_count DESC);

-- Comments table indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_comments_post_id ON comments(post_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_comments_user_id ON comments(user_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_comments_post_created ON comments(post_id, created_at DESC);

-- Post Likes table indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_post_likes_post_id ON post_likes(post_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_post_likes_user_id ON post_likes(user_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_post_likes_post_user ON post_likes(post_id, user_id);

-- Comment Likes table indexes  
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_comment_likes_comment_id ON comment_likes(comment_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_comment_likes_user_id ON comment_likes(user_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_comment_likes_comment_user ON comment_likes(comment_id, user_id);

-- Post Tags table indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_post_tags_post_id ON post_tags(post_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_post_tags_tag_id ON post_tags(tag_id);

-- Tags table indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_tags_tag_name ON tags(tag_name);

-- User Follow table indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_user_follow_following_user_id ON user_follow(following_user_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_user_follow_followed_user_id ON user_follow(followed_user_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_user_follow_following_followed ON user_follow(following_user_id, followed_user_id);

-- Spots table indexes
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_spots_spot_name ON spots(spot_name);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_spots_spot_level ON spots(spot_level);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_spots_location ON spots(latitude, longitude);

-- Comment Alarms table indexes (if exists)
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_comment_alarms_user_id ON comment_alarms(user_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_comment_alarms_post_id ON comment_alarms(post_id);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_comment_alarms_created_at ON comment_alarms(created_at DESC);

-- Partial indexes for specific query patterns
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_posts_active_by_spot ON posts(spot_id, created_at DESC) 
WHERE like_count > 0;

CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_posts_popular ON posts(like_count DESC, created_at DESC) 
WHERE like_count > 5;

-- Composite indexes for complex queries
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_posts_full_search ON posts(user_id, spot_id, created_at DESC);
CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_user_activity ON user_follow(following_user_id) 
INCLUDE (followed_user_id);
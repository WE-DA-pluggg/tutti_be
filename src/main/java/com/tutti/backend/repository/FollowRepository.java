package com.tutti.backend.repository;

import com.tutti.backend.domain.Follow;
import com.tutti.backend.domain.User;
import com.tutti.backend.dto.user.FollowingDtoMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository <Follow, Long> {
    Long countByUser(User user);
    Long countByFollowingUser(User user);

    List<FollowingDtoMapping> findByUser(User user); // 팔로잉 전체 불러오기
    List<FollowingDtoMapping> findTop7ByUser(User user);
    List<FollowingDtoMapping> findAllByUser_Artist(String user);
}

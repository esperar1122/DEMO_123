package com.campus.secondhand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.campus.secondhand.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {
    
    @Select("SELECT COUNT(*) FROM reviews WHERE order_id = #{orderId} AND reviewer_id = #{reviewerId}")
    int countByOrderAndReviewer(@Param("orderId") Long orderId, @Param("reviewerId") Long reviewerId);
}
package com.example.classhubproject.mapper.enrollmentinfo;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EnrollmentInfoMapper {

    void insertEnrollmentInfo(int userId, int classId, int enrollmentFee);

}

package com.example.classhubproject.mapper.user;

import com.example.classhubproject.data.user.UserResponseDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    Integer joinByGoogle(UserResponseDTO userResponseDTO);

    Integer selectUserIDBySnsId(String snsId);

    Integer checkDuplicateBySnsId(String snsId);

    Integer join(UserResponseDTO userDTO);

    UserResponseDTO selectUserBySnsId(String snsId);
}

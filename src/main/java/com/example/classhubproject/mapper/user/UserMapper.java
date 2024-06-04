package com.example.classhubproject.mapper.user;

import com.example.classhubproject.data.user.UserResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    Integer joinByGoogle(UserResponseDTO userResponseDTO);

    Integer selectUserIDByUsername(String username);

    Integer checkDuplicateByUsername(String username);

    Integer join(UserResponseDTO userDTO);

    UserResponseDTO selectUserByUsername(String username);

    void updateUserInfo(UserResponseDTO user);

    void updateUserImage(@Param("username") String snsId, @Param("file") String file);

    Integer insertGoogle(UserResponseDTO userDTO);

    Integer updateGoogle(UserResponseDTO userDTO);

    int getUserId(String userName);

    String selectUserNameByUsername(String username);
}

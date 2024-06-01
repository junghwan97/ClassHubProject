package com.example.classhubproject.mapper.user;

import com.example.classhubproject.data.user.UserRequestDTO;
import com.example.classhubproject.data.user.UserResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    Integer joinByGoogle(UserRequestDTO userRequestDTO);

    Integer selectUserIDBySnsId(String snsId);

    Integer checkDuplicateBySnsId(String snsId);

    Integer join(UserResponseDTO userDTO);

    UserResponseDTO selectUserBySnsId(String snsId);

    void updateUserInfo(UserResponseDTO user);

    void updateUserImage(@Param("snsId") String snsId, @Param("file") String file);

    UserResponseDTO selectUserByUsername(String username);

    void updateGoogleAccount(UserResponseDTO existData);
}

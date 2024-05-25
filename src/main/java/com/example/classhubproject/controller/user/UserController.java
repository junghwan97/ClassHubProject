package com.example.classhubproject.controller.user;

import com.example.classhubproject.data.common.ResponseData;
import com.example.classhubproject.data.common.ResponseMessage;
import com.example.classhubproject.data.user.UserResponseDTO;
import com.example.classhubproject.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Tag(name = "회원 기능 모음", description = "회원 관련 기능을 처리")
@RestController
//@RequestMapping(produces = "application/json")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "구글 로그인으로 회원가입",
            description = "구글 로그인을 통해 회원가입 할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "구글 로그인으로 회원가입 성공", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "구글 로그인으로 회원가입 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping(value = "/login/oauth2/code/{registrationId}", produces = "application/json")
    public ResponseEntity<ResponseData<UserResponseDTO>> googleLogin(@RequestParam("code") String code, @PathVariable(value = "registrationId") String registrationId) {
        Map<Integer, UserResponseDTO> user = userService.socialLogin(code, registrationId);
        Set<Integer> key = user.keySet();
        UserResponseDTO result = user.get(1);

        if (key.contains(1)) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.JOIN_SUCCESS, result));

        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseData.res(HttpStatus.CONFLICT.value(), ResponseMessage.JOIN_ERROR));
        }
    }

    @Operation(summary = "구글 로그인후 db에 저장",
            description = "구글 로그인을 통해 나온 값을 db에 저장할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "구글 로그인으로 회원가입 성공", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "구글 로그인으로 회원가입 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("join")
    public ResponseEntity<ResponseData<Void>> join(@RequestBody UserResponseDTO userDTO) {
        int result = userService.join(userDTO);
        if (result == 1) {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.JOIN_SUCCESS));
        } else {
            return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.JOIN_DUPLICATE));
        }
    }

    @Operation(summary = "회원 조회",
            description = "구글 id를 통해 회원 조회를 할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원 조회 성공", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "회원 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("selectUser")
    public ResponseEntity<ResponseData<UserResponseDTO>> selectUser(@RequestParam("snsId") String snsId) {
        UserResponseDTO user = userService.selectUserBySnsId(snsId);
        return ResponseEntity.ok(ResponseData.res(HttpStatus.OK.value(), ResponseMessage.SELECT_USER, user));
    }

//    @PostMapping("updateUserInfo")
//    public ResponseEntity<ResponseData<UserResponseDTO>> updateUser(@RequestParam("snsId") String snsId) {
//        UserResponseDTO user = userService.selectUserBySnsId(snsId);
//        userService.updateUserInfo(user);
//    }
}
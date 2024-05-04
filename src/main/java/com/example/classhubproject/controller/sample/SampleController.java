package com.example.classhubproject.controller.sample;

import com.example.classhubproject.data.sample.SampleDto;
import com.example.classhubproject.mapper.sample.SampleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SampleController {

    private final SampleMapper mapper;

    @Autowired
    public SampleController(SampleMapper mapper) {
        this.mapper = mapper;
    }

    @Operation(summary = "샘플 조회", description = "샘플을 조회할 수 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "샘플 조회 성공", content = @Content(schema = @Schema(implementation = SampleDto.class))),
                    @ApiResponse(responseCode = "400", description = "샘플 조회 실패", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("selectAll")
    public List<SampleDto> getAll() {
        return mapper.getAll();
    }
}

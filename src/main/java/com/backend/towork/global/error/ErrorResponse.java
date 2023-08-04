package com.backend.towork.global.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Builder
public class ErrorResponse {

    @JsonIgnore
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Schema(example = "400", description = "에러가 난 http status를 나타냅니다.")
    private final int status;

    @Schema(description = "에러가 난 이유를 나타냅니다.")
    private final String message;

    public String convertToJson() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }

}

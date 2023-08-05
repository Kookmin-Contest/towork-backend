package com.backend.towork.workspace.domain.dto.response;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WorkspaceResponseDto {

    private Long id;

    private String workspaceName;

}

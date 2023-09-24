package com.towork.api.workspace.domain.dto.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class WorkspaceResponseDto {

    private Long id;

    private String workspaceName;

}

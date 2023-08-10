package com.backend.towork.workspace.domain.mapper;

import com.backend.towork.workspace.domain.dto.response.WorkspaceResponseDto;
import com.backend.towork.workspace.domain.entity.Workspace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WorkspaceMapper {

    WorkspaceMapper INSTANCE = Mappers.getMapper(WorkspaceMapper.class);

    @Mapping(source = "name", target = "workspaceName")
    WorkspaceResponseDto toResponseDto(Workspace workspace);
}

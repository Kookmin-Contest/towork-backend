package com.towork.api.member.domain.mapper;

import com.towork.api.member.domain.dto.request.MemberRequestDto;
import com.towork.api.member.domain.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Mapper
public interface MemberMapper {

    public static final MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "password", target = "password", qualifiedByName = "encryptPassword")
    @Mapping(source = "birthDate", target = "birthDate", qualifiedByName = "parseLocalDate")
    @Mapping(target = "authProvider", constant = "SELF")
    @Mapping(target = "createDateTime", ignore = true)
    @Mapping(target = "workspaces", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "role", constant = "USER")
    Member requestDtoToEntity(MemberRequestDto memberRequestDto);

    @Named("encryptPassword")
    default String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Named("parseLocalDate")
    default LocalDate parseLocalDate(String exp) {
        return LocalDate.parse(exp);
    }

}

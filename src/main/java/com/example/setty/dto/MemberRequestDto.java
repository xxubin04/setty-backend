package com.example.setty.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "회원 추가 요청 DTO")
public class MemberRequestDto {

    @Schema(description = "회원 닉네임", required = true, example = "Tome")
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @Schema(description = "회원 이메일", required = true, example = "xxubin@gmail.com")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Schema(description = "비밀번호", required = true, example = "1234")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    @Schema(description = "회원 계정 타입", required = true, example = "CONSUMER")
    @NotBlank(message = "계정 타입은 필수 입력 값입니다.")
    @Pattern(regexp = "^(CONSUMER|SELLER)$", message = "계정 타입은 CONSUMER, SELLER 중 하나여야 합니다.")
    private String accountType;
}

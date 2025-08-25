package team.mjk.agent.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import team.mjk.agent.domain.member.dto.response.EncryptedMemberInfoResponse;
import team.mjk.agent.global.util.KmsUtil;

public record MemberInfoSaveRequest(

        @NotBlank(message = "이름을 입력해주세요.")
        String name,

        @NotBlank(message = "영문 이름을 입력해주세요.")
        String firstName,

        @NotBlank(message = "영문 성을 입력해주세요.")
        String lastName,

        @NotBlank(message = "핸드폰 번호를 입력해주세요.")
        String phoneNumber,

        @NotBlank(message = "성별을 선택해주세요.")
        String gender,

        @NotNull(message = "생일을 선택해주세요.")
        String birthDate,

        String passportNumber,

        String passportExpireDate
) {
        public EncryptedMemberInfoResponse encryptWith(KmsUtil kmsUtil) {
                return EncryptedMemberInfoResponse.builder()
                        .name(name)
                        .firstName(kmsUtil.encrypt(firstName))
                        .lastName(kmsUtil.encrypt(lastName))
                        .phoneNumber(kmsUtil.encrypt(phoneNumber))
                        .gender(gender)
                        .birthDate(kmsUtil.encrypt(birthDate))
                        .passportNumber(kmsUtil.encrypt(passportNumber))
                        .passportExpireDate(kmsUtil.encrypt(passportExpireDate))
                        .build();
        }
}
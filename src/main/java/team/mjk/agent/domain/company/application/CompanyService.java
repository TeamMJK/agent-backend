package team.mjk.agent.domain.company.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.mjk.agent.domain.company.domain.CompanyRepository;
import team.mjk.agent.domain.invitation.InvitationCodeProvider;
import team.mjk.agent.domain.invitation.domain.Invitation;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final InvitationCodeProvider invitationCodeProvider;
    private final CompanyRepository companyRepository;

    public Invitation createInvitationCode(Long companyId) {
        // TODO: 커스텀 예외 처리 하세용
        if(companyRepository.findById(companyId).isEmpty()) {
            throw new IllegalArgumentException("Company not found with id: " + companyId);
        }

        return invitationCodeProvider.create(companyId);
    }


}
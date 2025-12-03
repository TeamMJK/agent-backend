package team.mjk.agent.domain.company.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.company.application.dto.request.CompanyInvitationCodeServiceRequest;
import team.mjk.agent.domain.company.application.dto.request.CompanyInvitationEmailServiceRequest;
import team.mjk.agent.domain.company.application.dto.request.CompanySaveServiceRequest;
import team.mjk.agent.domain.company.application.dto.request.CompanyUpdateServiceRequest;
import team.mjk.agent.domain.company.application.dto.response.CompanyInvitationEmailResponse;
import team.mjk.agent.domain.company.application.dto.response.CompanyJoinResponse;
import team.mjk.agent.domain.company.application.dto.response.CompanySaveResponse;
import team.mjk.agent.domain.company.application.dto.response.CompanyUpdateResponse;
import team.mjk.agent.domain.company.domain.Company;
import team.mjk.agent.domain.company.domain.CompanyRepository;
import team.mjk.agent.domain.companyworkspace.application.command.CompanyWorkspaceCommandService;
import team.mjk.agent.domain.invitation.domain.Invitation;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.notion.application.command.NotionCommandService;
import team.mjk.agent.domain.notion.domain.Notion;
import team.mjk.agent.domain.notion.domain.NotionRepository;
import team.mjk.agent.domain.slack.application.command.SlackCommandService;
import team.mjk.agent.domain.slack.domain.Slack;
import team.mjk.agent.domain.slack.domain.SlackRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CompanyCommandService {

    private final CompanyRepository companyRepository;
    private final MemberRepository memberRepository;
    private final CompanyWorkspaceCommandService companyWorkspaceCommandService;
    private final NotionCommandService notionCommandService;
    private final SlackCommandService slackCommandService;
    private final CompanyInvitationService companyInvitationService;
    private final NotionRepository notionRepository;
    private final SlackRepository slackRepository;
    private final CompanyDeleteService companyDeleteService;

    @Transactional
    public CompanySaveResponse saveCompany(CompanySaveServiceRequest request) {
        Member member = memberRepository.findByMemberId(request.memberId());

        Company company = Company.create(request.name());
        company = companyRepository.save(company);

        companyWorkspaceCommandService.createWorkspaces(company, request.workspaces());

        member.saveCompany(company);

        if (request.notionConfigSaveRequest() != null) {
            notionCommandService.saveNotion(
                    request.notionConfigSaveRequest().toServiceRequest(company.getId())
            );
        }

        if (request.slackConfigSaveRequest() != null) {
            slackCommandService.saveSlack(
                    request.slackConfigSaveRequest().toServiceRequest(company.getId())
            );
        }

        return CompanySaveResponse.builder()
                .companyId(company.getId())
                .build();
    }

    public CompanyInvitationEmailResponse createInvitationCodeAndSendEmail(
            CompanyInvitationEmailServiceRequest request
    ) {
        Member member = memberRepository.findByMemberId(request.memberId());
        Long companyId = member.getCompany().getId();
        Company company = companyRepository.findByCompanyId(companyId);

        return companyInvitationService.createInvitation(company.getId(), request.email());
    }

    @Transactional
    public CompanyJoinResponse join(CompanyInvitationCodeServiceRequest request) {
        Member member = memberRepository.findByMemberId(request.memberId());

        Invitation invitation = companyInvitationService.getByCode(request.invitationCode());
        Company company = companyRepository.findByCompanyId(invitation.getCompanyId());

        member.saveCompany(company);
        companyInvitationService.delete(invitation);

        return CompanyJoinResponse.builder()
                .companyName(company.getName())
                .build();
    }

    @Transactional
    public CompanyUpdateResponse update(CompanyUpdateServiceRequest request) {
        Member member = memberRepository.findByMemberId(request.memberId());
        Long companyId = member.getCompany().getId();
        Company company = companyRepository.findByCompanyId(companyId);

        if (request.notionConfigUpdateRequest() != null) {
            notionCommandService.updateNotion(
                    request.notionConfigUpdateRequest().toServiceRequest(company.getId())
            );
        } else {
            Optional<Notion> notion = notionRepository.findOptionalByCompanyId(company.getId());
            notion.ifPresent(notionRepository::delete);
        }

        if (request.slackConfigUpdateRequest() != null) {
            slackCommandService.updateSlack(
                    request.slackConfigUpdateRequest().toServiceRequest(company.getId())
            );
        } else {
            Optional<Slack> slack = slackRepository.findOptionalByCompanyId(company.getId());
            slack.ifPresent(slackRepository::delete);
        }

        company.updateName(request.name());
        companyWorkspaceCommandService.updateWorkspaces(company, request.workspaces());

        return CompanyUpdateResponse.builder()
                .companyId(company.getId())
                .build();
    }

    @Transactional
    public Long delete(Long memberId) {
        Member member = memberRepository.findByMemberId(memberId);
        Company company = member.getValidatedCompany();

        companyDeleteService.deleteCompany(company, memberId);
        companyRepository.delete(company);

        return company.getId();
    }

}

package team.mjk.agent.domain.notion.application;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.mjk.agent.domain.businessTrip.dto.request.BusinessTripSaveRequest;
import team.mjk.agent.domain.company.domain.Workspace;
import team.mjk.agent.domain.member.domain.Member;
import team.mjk.agent.domain.member.domain.MemberRepository;
import team.mjk.agent.domain.member.presentation.exception.MemberNotFoundException;
import team.mjk.agent.domain.notion.domain.Notion;
import team.mjk.agent.domain.notion.domain.NotionRepository;
import team.mjk.agent.domain.notion.dto.request.NotionTokenRequest;
import team.mjk.agent.domain.notion.dto.request.NotionTokenUpdateRequest;
import team.mjk.agent.domain.notion.presentation.exception.NotionAPIException;
import team.mjk.agent.global.mcp.McpService;
import okhttp3.Request;
import okhttp3.RequestBody;

@RequiredArgsConstructor
@Service
public class NotionService implements McpService {

  private final NotionRepository notionRepository;
  private final MemberRepository memberRepository;
  private final ObjectMapper objectMapper;

  public Long save(Long memberId, NotionTokenRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);
    Long companyId = member.getCompany().getId();

    Notion notion = Notion.create(request.token(), request.databaseId(), companyId);
    notionRepository.save(notion);
    return notion.getId();
  }

  @Transactional
  public Long update(Long memberId, NotionTokenUpdateRequest request) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(MemberNotFoundException::new);
    Long companyId = member.getCompany().getId();

    Notion notion = notionRepository.findByCompanyId(companyId);
    notion.update(request.token(),request.databaseId());
    return notion.getId();
  }

  @Override
  public void create(BusinessTripSaveRequest request, Long companyId) {
    Notion notion = notionRepository.findByCompanyId(companyId);

    String url = "https://api.notion.com/v1/pages";
    OkHttpClient client = new OkHttpClient();

    List<Map<String, Map<String, String>>> nameBlocks = request.names().stream()
        .map(name -> Map.of("text", Map.of("content", name)))
        .toList();

    Map<String, Object> payload = Map.of(
        "parent", Map.of("database_id", notion.getDatabaseId()),
        "properties", Map.of(
            "이름", Map.of("title", nameBlocks),
            "도착일자", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.arriveDate().toString()))
            )),
            "출발일자", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.departDate().toString()))
            )),
            "출장지", Map.of("rich_text", List.of(
                Map.of("text", Map.of("content", request.destination()))
            ))
        )
    );

    String json = null;
    try {
      json = objectMapper.writeValueAsString(payload);
    } catch (JsonProcessingException e) {
      throw new NotionAPIException(e);
    }

    RequestBody body = RequestBody.create(
        json,
        MediaType.get("application/json; charset=utf-8")
    );

    Request httpRequest = new Request.Builder()
        .url(url)
        .addHeader("Authorization", "Bearer " + notion.getToken())
        .addHeader("Content-Type", "application/json")
        .addHeader("Notion-Version", "2022-06-28")
        .post(body)
        .build();

    try (Response response = client.newCall(httpRequest).execute()) {
      if (!response.isSuccessful()) {
        throw new NotionAPIException();
      }
    } catch (IOException e) {
      throw new NotionAPIException(e);
    }
  }

  @Override
  public Workspace getWorkspace() {
    return Workspace.NOTION;
  }
}

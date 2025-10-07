package team.mjk.agent.global.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import team.mjk.agent.domain.receipt.presentation.exception.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
@Component
public class S3Provider {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Value("${cloud.aws.cloudfront.domain}")
    private String cloudFrontDomain;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");

    public String upload(MultipartFile image) {
        validateImageFileExtension(image.getOriginalFilename());
        try {
            String key = uploadToS3(image);
            return cloudFrontDomain + "/" + key;
        } catch (IOException e) {
            throw new OnImageUploadExceptionCode();
        }
    }

    public void delete(String imageAddress) {
        String key = extractKeyFromUrl(imageAddress);
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build());
        } catch (Exception e) {
            throw new OnImageDeleteExceptionCode();
        }
    }

    private String uploadToS3(MultipartFile image) throws IOException {
        String originalFilename = image.getOriginalFilename();
        String extension = Objects.requireNonNull(originalFilename)
                .substring(originalFilename.lastIndexOf(".") + 1)
                .toLowerCase();

        String key = UUID.randomUUID().toString().substring(0, 10) + "_" +
                originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");

        try (InputStream inputStream = image.getInputStream()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType("image/" + extension)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, image.getSize()));
        } catch (Exception e) {
            throw new PutObjectExceptionCode();
        }

        return key;
    }

    public byte[] getObjectBytes(String key) {
        try {
            return s3Client.getObjectAsBytes(
                    software.amazon.awssdk.services.s3.model.GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build()
            ).asByteArray();
        } catch (Exception e) {
            throw new OnImageDownloadException();
        }
    }

    public String extractKeyFromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            return URLDecoder.decode(url.getPath().substring(1), UTF_8);
        } catch (MalformedURLException e) {
            throw new OnImageDeleteExceptionCode();
        }
    }

    private void validateImageFileExtension(String filename) {
        String extension = extractExtension(filename)
                .orElseThrow(NoFileExtensionExceptionCode::new);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new InvalidFileExtensionExceptionCode();
        }
    }

    private Optional<String> extractExtension(String filename) {
        int idx = filename.lastIndexOf(".");
        if (idx == -1 || idx == filename.length() - 1) {
            return Optional.empty();
        }
        return Optional.of(filename.substring(idx + 1).toLowerCase());
    }

}

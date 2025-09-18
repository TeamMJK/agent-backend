package team.mjk.agent.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.DecryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.EncryptionAlgorithmSpec;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class KmsUtil {

    private final KmsClient kmsClient;

    @Value("${aws.kms.key-id}")
    private String KEY_ID;

    public String encrypt(String token) {
        try {
            EncryptRequest request = EncryptRequest.builder()
                    .keyId(KEY_ID)
                    .plaintext(SdkBytes.fromByteArray(token.getBytes(StandardCharsets.UTF_8)))
                    .encryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256)
                    .build();

            var result = kmsClient.encrypt(request);
            byte[] cipherBytes = result.ciphertextBlob().asByteArray();

            return Base64.getEncoder().encodeToString(cipherBytes);
        } catch (Exception e) {
            log.error("Encryption failed", e);
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String token) {
        try {
            byte[] decoded = Base64.getDecoder().decode(token);

            DecryptRequest request = DecryptRequest.builder()
                    .ciphertextBlob(SdkBytes.fromByteArray(decoded))
                    .keyId(KEY_ID)
                    .encryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256)
                    .build();

            var result = kmsClient.decrypt(request);
            byte[] plainBytes = result.plaintext().asByteArray();

            return new String(plainBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Decryption failed", e);
            throw new RuntimeException("Decryption failed", e);
        }
    }

}

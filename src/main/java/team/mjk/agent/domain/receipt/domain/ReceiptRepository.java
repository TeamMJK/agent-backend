package team.mjk.agent.domain.receipt.domain;

public interface ReceiptRepository {

    void save(Receipt receipt);

    Receipt findByUrl(String url);

}

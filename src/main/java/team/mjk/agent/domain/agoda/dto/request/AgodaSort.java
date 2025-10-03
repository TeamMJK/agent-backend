package team.mjk.agent.domain.agoda.dto.request;

import lombok.Getter;

@Getter
public enum AgodaSort {

  RECOMMENDED("Recommended"),
  PRICE_DESC("PriceDesc"),
  PRICE_ASC("PriceAsc"),
  STAR_RATING_DESC("StarRatingDesc"),
  STAR_RATING_ASC("StarRatingAsc"),
  ALL_GUESTS_REVIEW_SCORE("AllGuestsReviewScore"),
  BUSINESS_TRAVELLER_REVIEW_SCORE("BusinessTravellerReviewScore"),
  COUPLES_REVIEW_SCORE("CouplesReviewScore"),
  SOLO_TRAVELLERS_REVIEW_SCORE("SoloTravllersReviewScore"),
  FAMILIES_WITH_YOUNG_REVIEW_SCORE("FamiliesWithYoungReviewScore"),
  FAMILIES_WITH_TEEN_REVIEW_SCORE("FamiliesWithTeenReviewScore"),
  GROUPS_REVIEW_SCORE("GroupsReviewScore");

  private final String value;

  AgodaSort(String value) {
    this.value = value;
  }

}

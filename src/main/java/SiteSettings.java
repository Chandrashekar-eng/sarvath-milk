

import jakarta.persistence.*;

@Entity
@Table(name = "site_settings")
public class SiteSettings {
    @Id
    private Long id = 1L;

    private String brandName = "Sarvath";
    @Column(length = 1000)
    private String logoUrl = "";

    private String founderName = "Yashwanth kumar";
    @Column(length = 1000)
    private String founderImageUrl = "";

    private String primaryPhone = "9618965566";
    private String secondaryPhone = "9618965566";
    private String contactEmail = "sarvathdairy@gmail.com";
    private String location = "Bijinapally";
    private String youtubeUrl = "https://youtube.com";

    @Column(length = 3000)
    private String appLinksJson = "[{\"label\":\"YouTube Farm\",\"url\":\"https://youtube.com\"},{\"label\":\"WhatsApp\",\"url\":\"https://wa.me\"}]";

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getFounderName() { return founderName; }
    public void setFounderName(String founderName) { this.founderName = founderName; }
    public String getFounderImageUrl() { return founderImageUrl; }
    public void setFounderImageUrl(String founderImageUrl) { this.founderImageUrl = founderImageUrl; }
    public String getPrimaryPhone() { return primaryPhone; }
    public void setPrimaryPhone(String primaryPhone) { this.primaryPhone = primaryPhone; }
    public String getSecondaryPhone() { return secondaryPhone; }
    public void setSecondaryPhone(String secondaryPhone) { this.secondaryPhone = secondaryPhone; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getYoutubeUrl() { return youtubeUrl; }
    public void setYoutubeUrl(String youtubeUrl) { this.youtubeUrl = youtubeUrl; }
    public String getAppLinksJson() { return appLinksJson; }
    public void setAppLinksJson(String appLinksJson) { this.appLinksJson = appLinksJson; }
}
